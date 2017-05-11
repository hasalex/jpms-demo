package fr.sw.fwk.web;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Part {
    private Map<String, String> headers;
    private byte[] content;

    Map<String, String> extractHeaders(String headerLines) {
        headers = Arrays.stream(headerLines.split("\r\n"))
                .flatMap(s -> Arrays.stream(s.split(";")))
                .map(this::splitProp)
                .collect(Collectors.toMap(Property::getKey, Property::getValue));

        return headers;
    }

    private Property splitProp(String s) {
        if (s.contains(":")) {
            return new Property(s.split(":"));
        } else if (s.contains("=")) {
            return new Property(s.split("="));
        } else {
            return null;
        }
    }

    void setContent(byte[] content) {
        this.content = content;
    }

    public boolean hasHeader(String name) {
        return headers.get(name) != null;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public byte[] getContent() {
        return content;
    }

    private class Property {
        private final String key;
        private final String value;

        Property(String[] split) {
            this.key = trim(split[0]);
            this.value = trim(split[1]);
        }

        private String trim(String text) {
            String trimmedText = text.trim();
            return trimQuotes(trimmedText);
        }

        String getKey() {
            return key;
        }

        String getValue() {
            return value;
        }

        private String trimQuotes(String trimmedText) {
            return trimmedText.startsWith("\"") && trimmedText.endsWith("\"") ? trimmedText.substring(1, trimmedText.length() - 1) : trimmedText;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }




}
