package com.abdav.giri_guide.constant;

public class PathApi {
    private PathApi() {
    }

    public static final String GIRI_GUIDE_APP = "/api/v1";

    public static final String AUTH_API = GIRI_GUIDE_APP + "/auth";
    public static final String REGISTER_API = "/register";
    public static final String LOGIN_API = "/login";

    public static final String MOUNTAINS_API = GIRI_GUIDE_APP + "/mountains";

    public static final String CUSTOMER_API = GIRI_GUIDE_APP + "/customers";

    public static final String PROFILE_API = GIRI_GUIDE_APP + "/profile";

    public static final String TRANSACTIONS_API = GIRI_GUIDE_APP + "/transactions";
    public static final String PAYMENTS_API = "/payment";

    public static final String PROFILE_IMAGE_API = "/{id}/image";

    public static final String TOUR_GUIDE_API = GIRI_GUIDE_APP + "/tour-guide";

    public static final String CHANGE_PASSWORD_API = "/change-password/{userId}";
}
