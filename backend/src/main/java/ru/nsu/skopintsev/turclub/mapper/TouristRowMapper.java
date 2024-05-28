package ru.nsu.skopintsev.turclub.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.nsu.skopintsev.turclub.models.Contacts;
import ru.nsu.skopintsev.turclub.models.Tourist;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TouristRowMapper implements RowMapper<Tourist> {
    @Override
    public Tourist mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tourist tourist = new Tourist();
        tourist.setId(rs.getInt("id"));
        tourist.setFullName(rs.getString("full_name"));
        tourist.setGender(rs.getString("gender"));
        tourist.setBirthday(rs.getDate("birthday"));
        tourist.setCategory(rs.getInt("category"));

        Tourist.TouristType type = new Tourist.TouristType();
        type.setId(rs.getInt("type_id"));
        type.setName(rs.getString("type_name"));
        tourist.setType(type);

        Contacts contacts = new Contacts();
        contacts.setId(rs.getInt("contact_id"));
        contacts.setEmail(rs.getString("email"));
        contacts.setMainPhone(rs.getString("main_phone"));
        contacts.setReservePhone(rs.getString("reserve_phone"));
        contacts.setEmergencyPhone(rs.getString("emergency_phone"));
        tourist.setContacts(contacts);

        return tourist;
    }
}
