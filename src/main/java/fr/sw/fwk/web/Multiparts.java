package fr.sw.fwk.web;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Multiparts {
    private static final byte CR = 0x0D;
    private static final byte LF = 0x0A;
    private static final byte DASH = 0x2D;
    private static final int HEADER_PART_SIZE_MAX = 10240;
    private static final int DEFAULT_BUFSIZE = 4096;
    private static final byte[] HEADER_SEPARATOR = {CR, LF, CR, LF };
    private static final byte[] FIELD_SEPARATOR = {CR, LF};
    private static final byte[] STREAM_TERMINATOR = {DASH, DASH};
    private static final byte[] BOUNDARY_PREFIX = {CR, LF, DASH, DASH};

    private final InputStream input;
    private int boundaryLength;
    private int keepRegion;
    private byte[] boundary;
    private final byte[] buffer;
    private int head;
    private int tail;

    private Multiparts(HttpRequest request) {
        this.input = request.getBody();
        this.buffer = new byte[DEFAULT_BUFSIZE];

        byte[] boundary = extractAttr(request.getHeader("Content-Type"), "boundary").getBytes();
        this.boundary = new byte[boundary.length + BOUNDARY_PREFIX.length];
        this.boundaryLength = boundary.length + BOUNDARY_PREFIX.length;
        this.keepRegion = this.boundary.length;
        System.arraycopy(BOUNDARY_PREFIX, 0, this.boundary, 0, BOUNDARY_PREFIX.length);
        System.arraycopy(boundary, 0, this.boundary, BOUNDARY_PREFIX.length, boundary.length);

        head = 0;
        tail = 0;
    }

    public static List<Part> parse(HttpRequest request) {
        Multiparts multiparts = new Multiparts(request);
        try {
            List<Part> parts = new ArrayList<>();

            boolean nextPart = multiparts.skipPreamble();
            while(nextPart) {
                String bodyHeaders = multiparts.readHeaders();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                multiparts.readBodyData(outputStream);

                Part part = new Part();
                part.extractHeaders(bodyHeaders);
                part.setContent(outputStream.toByteArray());
                parts.add(part);

                nextPart = multiparts.readBoundary();
            }

            return parts;
        } catch (IOException e) {
            throw new HttpException(e);
        }

    }


    private byte readByte() throws IOException {
        if (head == tail) {
            head = 0;
            tail = input.read(buffer, head, DEFAULT_BUFSIZE);
            if (tail == -1) {
                throw new IOException("No more data is available");
            }
        }
        return buffer[head++];
    }

    private boolean readBoundary() {
        byte[] marker = new byte[2];
        boolean nextChunk;

        head += boundaryLength;
        try {
            marker[0] = readByte();
            if (marker[0] == LF) {
                return true;
            }

            marker[1] = readByte();
            if (arrayequals(marker, STREAM_TERMINATOR)) {
                nextChunk = false;
            } else if (arrayequals(marker, FIELD_SEPARATOR)) {
                nextChunk = true;
            } else {
                throw new MultipartException("Unexpected characters follow a boundary");
            }
        } catch (IOException e) {
            throw new MultipartException("Stream ended unexpectedly");
        }
        return nextChunk;
    }

    private String readHeaders() {
        int i = 0;
        byte b;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int size = 0;
        while (i < HEADER_SEPARATOR.length) {
            try {
                b = readByte();
            } catch (IOException e) {
                throw new MultipartException("Stream ended unexpectedly");
            }
            if (++size > HEADER_PART_SIZE_MAX) {
                throw new MultipartException(
                    "Header section has more than " + HEADER_PART_SIZE_MAX + " bytes (maybe it is not properly terminated)");
            }
            if (b == HEADER_SEPARATOR[i]) {
                i++;
            } else {
                i = 0;
            }
            baos.write(b);
        }

        return baos.toString();
    }


    private int readBodyData(OutputStream output) throws IOException {
        byte[] pBuffer = new byte[8192];
        InputStream in = new ItemInputStream();
        try {
            int total = 0;
            for (;;) {
                int res = in.read(pBuffer);
                if (res == -1) {
                    break;
                }
                if (res > 0) {
                    total += res;
                    if (output != null) {
                        output.write(pBuffer, 0, res);
                    }
                }
            }
            if (output != null) {
                output.flush();
            }
            in.close();
            in = null;
            return total;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable t) {
                    /* Ignore me */
                }
            }
        }
    }

    private boolean skipPreamble() throws IOException {
        System.arraycopy(boundary, 2, boundary, 0, boundary.length - 2);
        boundaryLength = boundary.length - 2;
        try {
            readBodyData(null);
            return readBoundary();
        } catch (MultipartException e) {
            return false;
        } finally {
            // Restore delimiter.
            System.arraycopy(boundary, 0, boundary, 2, boundary.length - 2);
            boundaryLength = boundary.length;
            boundary[0] = CR;
            boundary[1] = LF;
        }
    }

    private static boolean arrayequals(byte[] a, byte[] b) {
        for (int i = 0; i < 2; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    private int findByte(byte value, int pos) {
        for (int i = pos; i < tail; i++) {
            if (buffer[i] == value) {
                return i;
            }
        }

        return -1;
    }

    private int findSeparator() {
        int first;
        int match = 0;
        int maxpos = tail - boundaryLength;
        for (first = head;
        (first <= maxpos) && (match != boundaryLength);
        first++) {
            first = findByte(boundary[0], first);
            if (first == -1 || (first > maxpos)) {
                return -1;
            }
            for (match = 1; match < boundaryLength; match++) {
                if (buffer[first + match] != boundary[match]) {
                    break;
                }
            }
        }
        if (match == boundaryLength) {
            return first - 1;
        }
        return -1;
    }

    private String extractAttr(String contentType, String attr) {
        String value = contentType.substring(contentType.indexOf(attr + "=") + attr.length() + 1, contentType.length());
        return value.startsWith("\"") && value.endsWith("\"") ? value.substring(1, value.length() - 1) : value;
    }


    private static class MultipartException extends RuntimeException {
        private MultipartException(String message) {
            super(message);
        }
    }

    private class ItemInputStream extends InputStream {
        private int pad;
        private int pos;

        private ItemInputStream() {
            findSeparator();
        }

        private void findSeparator() {
            pos = Multiparts.this.findSeparator();
            if (pos == -1) {
                if (tail - head > keepRegion) {
                    pad = keepRegion;
                } else {
                    pad = tail - head;
                }
            }
        }

        public int available() throws IOException {
            if (pos == -1) {
                return tail - head - pad;
            }
            return pos - head;
        }

        public int read() throws IOException {
            if (available() == 0) {
                if (makeAvailable() == 0) {
                    return -1;
                }
            }
            int b = buffer[head++];
            if (b >= 0) {
                return b;
            }
            return b + 256;
        }

        public int read(byte[] b, int off, int len) throws IOException {
            if (len == 0) {
                return 0;
            }
            int res = available();
            if (res == 0) {
                res = makeAvailable();
                if (res == 0) {
                    return -1;
                }
            }
            res = Math.min(res, len);
            System.arraycopy(buffer, head, b, off, res);
            head += res;
            return res;
        }

        private int makeAvailable() throws IOException {
            if (pos != -1) {
                return 0;
            }

            System.arraycopy(buffer, tail - pad, buffer, 0, pad);

            head = 0;
            tail = pad;

            for (;;) {
                int bytesRead = input.read(buffer, tail, DEFAULT_BUFSIZE - tail);
                if (bytesRead == -1) {
                    final String msg = "Stream ended unexpectedly";
                    throw new MultipartException(msg);
                }
                tail += bytesRead;

                findSeparator();
                int av = available();

                if (av > 0 || pos != -1) {
                    return av;
                }
            }
        }

    }

}
