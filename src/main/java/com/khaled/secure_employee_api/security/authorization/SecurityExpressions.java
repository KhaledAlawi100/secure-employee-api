package com.khaled.secure_employee_api.security.authorization;

public final class SecurityExpressions {

    private SecurityExpressions() {
    }

    public static final class Employee {

        private Employee() {
        }

        /**
         * Reserved for future business/public employee endpoints.
         */
        public static final String READ =
                "hasAuthority('EMPLOYEE_READ')";

        /**
         * Employee management is currently an ADMIN feature.
         */
        public static final String CREATE =
                Roles.ADMIN;

        public static final String UPDATE =
                Roles.ADMIN;

        public static final String DELETE =
                Roles.ADMIN;
    }

    public static final class Department {

        private Department() {
        }

        public static final String READ =
                "hasAuthority('DEPARTMENT_READ')";

        public static final String CREATE =
                "hasAuthority('DEPARTMENT_CREATE')";

        public static final String UPDATE =
                "hasAuthority('DEPARTMENT_UPDATE')";

        public static final String DELETE =
                "hasAuthority('DEPARTMENT_DELETE')";
    }

    public static final class Position {

        private Position() {
        }

        public static final String READ =
                "hasAuthority('POSITION_READ')";

        public static final String CREATE =
                "hasAuthority('POSITION_CREATE')";

        public static final String UPDATE =
                "hasAuthority('POSITION_UPDATE')";

        public static final String DELETE =
                "hasAuthority('POSITION_DELETE')";
    }

    public static final class Roles {

        private Roles() {
        }

        public static final String ADMIN =
                "hasRole('ADMIN')";

        public static final String USER =
                "hasRole('USER')";
    }

    public static final class Common {

        private Common() {
        }

        public static final String AUTHENTICATED =
                "isAuthenticated()";
    }

}