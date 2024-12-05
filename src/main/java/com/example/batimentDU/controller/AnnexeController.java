package com.example.batimentDU.controller;


import com.example.batimentDU.dto.AnnexeDTO;
import com.example.batimentDU.model.Annexe;
import com.example.batimentDU.service.AnnexeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/annexe")
public class AnnexeController {

    @Autowired
    AnnexeService annexeService;

    @GetMapping("/getAnn")
    @ResponseBody
    public List<Annexe> getAnnexes(){
        return annexeService.findAll();
    }

    @GetMapping("/arrondissement/{arrondissementId}")
    @ResponseBody
    public List<AnnexeDTO> getAnnexesByArrondissement(@PathVariable Long arrondissementId) {
        List<Annexe> annexes = annexeService.findAnnexesByArrondissementId(arrondissementId);
        return annexes.stream()
                .map(annexe -> new AnnexeDTO(annexe.getIdAnn(), annexe.getNomAnnexe()))
                .collect(Collectors.toList());
    }

}
