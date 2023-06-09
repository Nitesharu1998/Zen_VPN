package com.countries.vpn.Vpn;

import java.util.regex.Pattern;

public class VpnValidator {

    public static boolean validateDNS(String dnsAddress) {
        String[] dnsAddressSplit = dnsAddress.split(",");
        if (dnsAddressSplit.length > 0) {
            for (int i = 0; i < dnsAddressSplit.length; i++) {
                if (!dnsValidated(dnsAddressSplit[i])) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private static boolean dnsValidated(String singleDns) {//8.8.8.8
        if (singleDns.matches("^[a-zA-Z]*$")) {
            return false;
        }
        return true;
    }

    public static boolean validateIpAddress(String ipAddress) {
        String[] ipAddressSplit = ipAddress.split(",");
        if (!validIpV4(ipAddressSplit[0]) && !validIpV6(ipAddressSplit[1])) {
            return false;
        }
        return true;
    }

    private static boolean validIpV6(String ipv6Address) {
        String ipv6Pattern = "^([0-9a-fA-F]{1,4}:){7}([0-9a-fA-F]{1,4})/(12[0-8]|1[01][0-9]|[0-9]{1,2})$";
        Pattern pattern = Pattern.compile(ipv6Pattern);
        return pattern.matcher(ipv6Address).matches();
    }

    private static boolean validIpV4(String ipv4Address) {
        String ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/(3[0-2]|[12]?[0-9])$";
        Pattern pattern = Pattern.compile(ipv4Pattern);
        return pattern.matcher(ipv4Address).matches();
    }
}
