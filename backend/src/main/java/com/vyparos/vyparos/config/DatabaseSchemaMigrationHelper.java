package com.vyparos.vyparos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSchemaMigrationHelper implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSchemaMigrationHelper.class);
    private final JdbcTemplate jdbcTemplate;

    public DatabaseSchemaMigrationHelper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            log.info("Running safe database column schema verification for attendance_records.status...");
            
            // Drop any old enum check constraints if they exist
            try {
                java.util.List<String> constraints = jdbcTemplate.query(
                        "SELECT CONSTRAINT_NAME FROM INFORMATION_SCHEMA.CONSTRAINTS WHERE UPPER(TABLE_NAME) = 'ATTENDANCE_RECORDS' AND (UPPER(COLUMN_LIST) LIKE '%STATUS%' OR UPPER(CHECK_EXPRESSION) LIKE '%COMPLETED%')",
                        (rs, rowNum) -> rs.getString("CONSTRAINT_NAME")
                );
                for (String cName : constraints) {
                    try {
                        jdbcTemplate.execute("ALTER TABLE attendance_records DROP CONSTRAINT " + cName);
                        log.info("Dropped legacy attendance constraint: {}", cName);
                    } catch (Exception e) {
                        log.debug("Notice dropping constraint {}: {}", cName, e.getMessage());
                    }
                }
            } catch (Exception e) {
                log.debug("Notice querying constraints: {}", e.getMessage());
            }

            // Ensure column type is VARCHAR(30)
            jdbcTemplate.execute("ALTER TABLE attendance_records ALTER COLUMN status VARCHAR(30) NOT NULL");
            log.info("Successfully verified attendance_records.status as VARCHAR(30)");
        } catch (Exception e) {
            log.info("Safe schema verification notice: {}", e.getMessage());
        }
    }
}
