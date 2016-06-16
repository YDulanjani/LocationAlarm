package yamuna.com.locationalarm;


public  class DistanceCalculator {

    /*::    unit = the unit you desire for results
    where: 'M' is statute miles (default)
   'K' is kilometers
   'N' is nautical miles
    :*/

    public static float distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

	/*::	This function converts decimal degrees to radians						 :*/

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }


	/*::	This function converts radians to decimal degrees						 :*/

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


}
