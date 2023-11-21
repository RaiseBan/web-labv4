package com.example.backend.service;

import com.example.backend.model.DataRequest;
import org.springframework.stereotype.Service;

@Service
public class AreaCheckService {
    public String isPointInsideArea(DataRequest data){
        double x = data.getX();
        double y = data.getY();
        double r = data.getR();
        if (x > 0 && y < 0){
            return hitTriangle(x, y, r);
        }
        if (x < 0 && y > 0) {
            return hitCircle(x, y, r);

        }
        if (x < 0 && y < 0){
            return hitSquare(x, y, r);
        }
        if ((x == 0 && y <= r/2 && y >= -r) || (y == 0 && x <= r && x >= -r)){
            return "In";
        }else{
            return "Out";
        }
    }
    public String hitSquare(double x, double y, double r) {
        if (x >= -r && y >= -r){
            return "In";
        }
        return "Out";
    } public String hitCircle(double x, double y, double r) {
        if (x*x + y*y <= r*r/4){
            return "In";
        }
        return "Out";
    }
    public String hitTriangle(double x, double y, double r) {
        if (y >= x - r){
            return "In";
        }
        return "Out";
    }

}
