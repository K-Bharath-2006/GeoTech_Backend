package com.example.GeoTech.Model;

import lombok.Data;

@Data
public class Officer {

    private String firebaseUid;

    // 👤 Basic info
    private String name;
    private String email;
    private String phone;
    private OfficerType officerType;   // ADMIN / OFFICER

    // 🗺️ Jurisdiction
    private String department;
    private String region;

    // 🔐 Status
    private boolean active;

    public enum OfficerType {
        ADMIN,
        OFFICER
    }
}
