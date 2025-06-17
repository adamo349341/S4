package util;

import model.PasswordEntry;
import network.NetworkPasswordEntry;

public class PasswordEntryMapper {

    public static NetworkPasswordEntry toNetworkModel(PasswordEntry entry) {
        return new NetworkPasswordEntry(
                entry.getService(),
                entry.getUsername(),
                entry.getPassword()
        );
    }

    public static PasswordEntry fromNetworkModel(NetworkPasswordEntry entry) {
        return new PasswordEntry(
                entry.getService(),
                entry.getUsername(),
                entry.getPassword()
        );
    }
}
