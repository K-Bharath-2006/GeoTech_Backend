package com.example.GeoTech.Service;

import com.example.GeoTech.Model.Officer;
import com.example.GeoTech.repo.OfficerRepository;
import org.springframework.stereotype.Service;

@Service
public class OfficerService {

    private final OfficerRepository officerRepository;

    public OfficerService(OfficerRepository officerRepository) {
        this.officerRepository = officerRepository;
    }

    public void createOfficer(Officer officer) throws Exception {
        System.out.println("Creating officer with email: " + officer.getEmail());

//        Officer existing = officerRepository.findByEmail(officer.getEmail());
//        if (existing != null) {
//            throw new RuntimeException("Officer already exists");
//        }

        officer.setFirebaseUid(null); // IMPORTANT
        officer.setActive(true);

        officerRepository.save(officer);
        System.out.println("Officer has been created");
    }

    // 🔐 Officer login logic (FIRST LOGIN LINKING)
    public Officer loginOfficer(String firebaseUid, String email) throws Exception {

        Officer officer = officerRepository.findByFirebaseUid(firebaseUid);
        System.out.println(officer);
        if (officer != null) {
            return officer;
        }

        // First-time login → link using email
        officer = officerRepository.findByEmail(email);

        if (officer == null) {
            throw new RuntimeException("Not an officer");
        }

        if (!officer.isActive()) {
            throw new RuntimeException("Officer inactive");
        }

        officer.setFirebaseUid(firebaseUid);
        officerRepository.save(officer);

        return officer;
    }

    public Officer validateAdmin(String firebaseUid, String email) throws Exception {

        Officer officer = officerRepository.findByFirebaseUid(firebaseUid);

        // 2️⃣ If not linked yet, try email (first login case)
        if (officer == null) {
            officer = officerRepository.findByEmail(email);
        }

        // 3️⃣ Officer must exist
        if (officer == null) {
            throw new SecurityException("NOT_AN_OFFICER");
        }

        // 4️⃣ Officer must be active
        if (!officer.isActive()) {
            throw new SecurityException("OFFICER_INACTIVE");
        }

        // 5️⃣ Officer must be ADMIN
        if (officer.getOfficerType() != Officer.OfficerType.ADMIN) {
            throw new SecurityException("NOT_ADMIN");
        }

        // 6️⃣ Link UID if first time
        if (officer.getFirebaseUid() == null) {
            officer.setFirebaseUid(firebaseUid);
            officerRepository.save(officer);
        }

        return officer;
    }

}
