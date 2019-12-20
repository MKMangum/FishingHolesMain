package com.mikekmangum.fishingholesmain;

public class DbSchema {

    public static final class CatchTable  {
        public static final String NAME = "Catches";

        public static final class Cols {

            public static final String CATCH_ID = "Catch_ID";
            public static final String TIME_OF_DAY = "Time";
            public static final String FISH_SPECIES = "Fish_Species";
            public static final String LENGTH = "Length";
            public static final String WEIGHT = "Weight";
            public static final String LURE = "Lure";
            public static final String LATITUDE = "Latitude";
            public static final String LONGITUDE = "Longitude";
            public static final String TEMPERATURE = "Temperature";
            public static final String CONDITIONS = "Conditions";
            public static final String PICTURE = "Picture";
        }
    }
}
