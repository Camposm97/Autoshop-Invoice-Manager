package model;

public class Strings {
    public static String toTitle(String s) {
        if (s.length() < 0) return s;
        StringBuilder sb = new StringBuilder();
        String[] tokens = s.trim().split("\\s+");
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (token.length() <= 0) continue;
            Character c = Character.toUpperCase(s.charAt(0));
            String t = c.toString();
            if (s.length() > 1) t += s.substring(1).toLowerCase();
            if (i == tokens.length - 1) sb.append(t);
            else sb.append(t + ' ');
        }
        return sb.toString();
    }

    @Deprecated
    public static String separateWords(String sentence) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentence.length(); i++) {
            char c = sentence.charAt(i);
            if (Character.isUpperCase(c) && i > 0)
                sb.append(' ');
            sb.append(c);
        }
        return sb.toString();
    }
}
