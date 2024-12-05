package com.example.batimentDU.controller;

import com.example.batimentDU.model.AppercuRapport;
import com.example.batimentDU.model.Arrondissement;
import com.example.batimentDU.service.AppercuRapportService;
import com.example.batimentDU.service.ArrondissementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/arrondissement")
public class ArrondissementController {

    @Autowired
    ArrondissementService arrondissementService;



    @GetMapping("/all")
    @ResponseBody
    public List<Arrondissement> getAllArrondissements() {
        return arrondissementService.findAll();
    }



}
