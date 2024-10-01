package com.example.kwic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@RestController
public class KwicApplication {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(KwicApplication.class, args);
    }

    // API to search for circular shifts from the database
    @GetMapping("/kwic/search")
    public List<String> searchKWIC(@RequestParam String query) {
        // Step 1: Query the database for input texts that match the search query
        String sql = "SELECT input_text FROM kwic_entries WHERE input_text LIKE ?";
        List<String> results = jdbcTemplate.queryForList(sql, new Object[]{"%" + query + "%"}, String.class);

        // Step 2: Generate circular shifts for all matching input texts
        List<String> allShifts = new ArrayList<>();
        for (String result : results) {
            allShifts.add(result);  // Add the original input text first
            allShifts.addAll(generateCircularShifts(result));  // Add n-1 shifts
        }

        // Step 3: Alphabetize the circular shifts
        Collections.sort(allShifts);

        // Step 4: Return the alphabetized list of circular shifts
        return allShifts;
    }

    // Method to generate n-1 circular shifts
    private List<String> generateCircularShifts(String input) {
        String[] words = input.split(" ");
        List<String> circularShifts = new ArrayList<>();
        int numShifts = words.length;  // n shifts, including the original

        // Generate n-1 circular shifts (excluding the original input if needed)
        for (int i = 1; i < numShifts; i++) {  // Start from 1 to exclude original input
            StringBuilder shift = new StringBuilder();
            for (int j = 0; j < words.length; j++) {
                shift.append(words[(i + j) % words.length]).append(" ");
            }
            circularShifts.add(shift.toString().trim()); // Add trimmed version of the shift
        }
        return circularShifts;
    }
}
