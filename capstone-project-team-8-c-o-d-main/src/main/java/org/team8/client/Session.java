package org.team8.client;

public final class Session {
    private static String token, role, username;
    private Session() {}
    public static void save(String t, String r, String u){ token=t; role=r; username=u; }
    public static String token(){ return token; }
    public static String role(){ return role; }
    public static String user(){ return username; }
    public static boolean isLoggedIn(){ return token != null && !token.isBlank(); }
    public static void clear(){ token = role = username = null; }
}