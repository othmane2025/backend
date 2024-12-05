package com.example.batimentDU.configuration;

import com.example.batimentDU.enumeraion.SituationChefFamille;
import com.example.batimentDU.enumeraion.SituationExploitant;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

@ControllerAdvice
public class EnumBinder {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Register custom editor for SituationChefFamille
        binder.registerCustomEditor(SituationChefFamille.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(SituationChefFamille.fromValue(text)); // Use your custom fromValue method
            }
        });

        // Register custom editor for SituationExploitant
        binder.registerCustomEditor(SituationExploitant.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(SituationExploitant.fromValue(text)); // Use your custom fromValue method
            }
        });
    }
}
