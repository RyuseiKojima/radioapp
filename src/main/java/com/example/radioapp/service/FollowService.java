package com.example.radioapp.service;

import com.example.radioapp.domain.AppUser;
import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.repository.RadioProgramRepository;
import com.example.radioapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    private final RadioProgramRepository programRepository;
    private final UserRepository userRepository;

    public FollowService(RadioProgramRepository programRepository, UserRepository userRepository) {
        this.programRepository = programRepository;
        this.userRepository = userRepository;
    }

    public void followProgram(Long programId, AppUser user) {
        RadioProgram program = programRepository.findById(programId)
                .orElseThrow(() -> new IllegalArgumentException("番組が見つかりません"));

        if (!user.getFollowedPrograms().contains(program)) {
            user.getFollowedPrograms().add(program);
            userRepository.save(user);
        }
    }
}