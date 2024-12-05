package com.example.batimentDU;

import com.example.batimentDU.enumeraion.*;
import com.example.batimentDU.model.*;
import com.example.batimentDU.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Date;
import java.time.LocalDate;

@SpringBootApplication
public class BatimentDuApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatimentDuApplication.class, args);
	}
/*
	@Bean
	CommandLineRunner runner(ArrondissementRepository arrondissementRepository,
							 AnnexeRepository annexeRepository,
							 BatimentRepository batimentRepository,
							 AppercuRapportRepository appercuRapportRepository,
							 CommuniquerDecisionRepository communiquerDecisionRepository,
							 DecisionCollectiveRepository decisionCollectiveRepository,
							 EtatAvancementRepository etatAvancementRepository,
							 ExpertiseTechniqueRepository expertiseTechniqueRepository,
							 InformerARRepository informerARRepository,
							 RaisonInobservationRepository raisonInobservationRepository,
							 RelogementRepository relogementRepository,
							 StatutActuelRepository statutActuelRepository){
		return args -> {
			Arrondissement arrondissement = new Arrondissement(NomArrondissement.BenMsick);
			arrondissementRepository.save(arrondissement);
			Annexe annexe = new Annexe(NomAnnexe.ANNEXE_59,arrondissement);
			annexeRepository.save(annexe);
			AppercuRapport appercuRapport = new AppercuRapport(12, LocalDate.of(2025,12,12),"test",15,LocalDate.of(2025,01,12),"");
			appercuRapportRepository.save(appercuRapport);
			StatutActuel statutActuel = new StatutActuel("vide","vacant",EntierementDemoli.ENTREPRISE, PartiellementDemoli.PROPRIETAIRE,"renforcer","restaurer");
			statutActuelRepository.save(statutActuel);
			Batiment batiment = new Batiment("el bouchra",20.0,"haut","othmane sebban",4,"khalid shouba",SituationChefFamille.LOCATAIRE,2,"karim bouchta",SituationExploitant.LOCATAIRE,"",annexe,statutActuel,appercuRapport);
            batimentRepository.save(batiment);
			Relogement relogement = new Relogement(2,1,"othmane sebban",1,"khalid shouba",LocalDate.of(2025,12,12),"casa","hahha",SocieteSurveillance.CASABLANCA,"teest");
			relogementRepository.save(relogement);
			InformerAR informerAR = new InformerAR(258,LocalDate.of(2026,12,25));
			informerARRepository.save(informerAR);
			EtatAvancement etatAvancement = new EtatAvancement(25,LocalDate.of(2026,12,25),14,LocalDate.of(2025,01,15),informerAR);
			etatAvancementRepository.save(etatAvancement);
			CommuniquerDecision communiquerDecision = new CommuniquerDecision(12,LocalDate.of(2026,02,28),relogement,etatAvancement);
			communiquerDecisionRepository.save(communiquerDecision);
			DecisionCollective decisionCollective = new DecisionCollective(25,LocalDate.of(2026,03,14),RecommandationDecision.RESTAURATION,"",communiquerDecision);
			decisionCollectiveRepository.save(decisionCollective);
			RaisonInobservation raisonInobservation = new RaisonInobservation("tesst",etatAvancement);
			raisonInobservationRepository.save(raisonInobservation);
			ExpertiseTechnique expertiseTechnique = new ExpertiseTechnique("LPEE",LocalDate.of(2025,02,01),RecommandationExpertise.BATIMENT_INTACT,"",appercuRapport,decisionCollective);
			expertiseTechniqueRepository.save(expertiseTechnique);
		};
	}*/

	/* @Bean
	CommandLineRunner runner(ArrondissementRepository arrondissementRepository,AnnexeRepository annexeRepository){
		return args -> {
			Arrondissement arrondissement = new Arrondissement(NomArrondissement.BenMsick);
			arrondissementRepository.save(arrondissement);
			Annexe annexe = new Annexe(NomAnnexe.ANNEXE_56,arrondissement);
			annexeRepository.save(annexe);
			Annexe annexe1 = new Annexe(NomAnnexe.ANNEXE_57,arrondissement);
			annexeRepository.save(annexe1);
			Annexe annexe2 = new Annexe(NomAnnexe.ANNEXE_57Bis,arrondissement);
			annexeRepository.save(annexe2);
			Annexe annexe3 = new Annexe(NomAnnexe.ANNEXE_58,arrondissement);
			annexeRepository.save(annexe3);
			Annexe annexe4= new Annexe(NomAnnexe.ANNEXE_58Bis,arrondissement);
			annexeRepository.save(annexe4);
			Annexe annexe5= new Annexe(NomAnnexe.ANNEXE_59,arrondissement);
			annexeRepository.save(annexe5);
			Arrondissement arrondissement1 = new Arrondissement(NomArrondissement.Sbata);
			arrondissementRepository.save(arrondissement1);
			Annexe annexe6= new Annexe(NomAnnexe.ANNEXE_60,arrondissement1);
			annexeRepository.save(annexe6);
			Annexe annexe7= new Annexe(NomAnnexe.ANNEXE_60Bis,arrondissement1);
			annexeRepository.save(annexe7);
			Annexe annexe8= new Annexe(NomAnnexe.ANNEXE_61,arrondissement1);
			annexeRepository.save(annexe8);
			Annexe annexe9= new Annexe(NomAnnexe.ANNEXE_62,arrondissement1);
			annexeRepository.save(annexe9);
			Annexe annexe10= new Annexe(NomAnnexe.ANNEXE_SAB,arrondissement1);
			annexeRepository.save(annexe10);

		};
	}*/

}
