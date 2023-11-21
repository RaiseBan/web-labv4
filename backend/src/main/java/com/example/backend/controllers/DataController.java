package com.example.backend.controllers;

import com.example.backend.DataBase.ShotProjection;
import com.example.backend.DataBase.ShotRepository;
import com.example.backend.Entity.Shot;
import com.example.backend.Entity.User;
import com.example.backend.Utils.JwtUtil;
import com.example.backend.model.DataRequest;
import com.example.backend.service.AreaCheckService;
import com.example.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DataController {
    private final AreaCheckService areaCheckService;
    private final ShotRepository shotRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    public DataController(AreaCheckService areaCheckService, ShotRepository shotRepository, JwtUtil jwtUtil, UserService userService){
        this.areaCheckService = areaCheckService;
        this.shotRepository = shotRepository;
        this.jwtUtil = jwtUtil;
        this.userService = userService;

    }
    @PostMapping("/checkArea")
    public String processData(@RequestBody DataRequest dataRequest, HttpServletRequest request) {
        String jwtToken = extractJwtFromRequest(request);
        String username = jwtUtil.extractUsername(jwtToken);
        User user = userService.findByUsername(username);
        String res = areaCheckService.isPointInsideArea(dataRequest);
        Shot newShot = new Shot();
        newShot.setX(dataRequest.getX());
        newShot.setY(dataRequest.getY());
        newShot.setR(dataRequest.getR());
        newShot.setResult(res);
        newShot.setUser(user);
        shotRepository.save(newShot);
        return res;
    }
    @Transactional
    @DeleteMapping("/clearTable")
    public ResponseEntity<?> clearTable(HttpServletRequest request){
        String jwtToken = extractJwtFromRequest(request);
        String username = jwtUtil.extractUsername(jwtToken);
        User user = userService.findByUsername(username);

        shotRepository.deleteAllByUser(user);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/user/data")
    public ResponseEntity<List<ShotProjection>> getUserData(HttpServletRequest request) {
        String jwtToken = extractJwtFromRequest(request);
        String username = jwtUtil.extractUsername(jwtToken);
        User user = userService.findByUsername(username);
        List<ShotProjection> userShots = shotRepository.findAllByUser(user);
        return ResponseEntity.ok(userShots);
    }
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
