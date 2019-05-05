package com.seenetuvastaja.seenetuvastaja.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MushroomDAO {

    private List<Mushroom> mushrooms = Arrays.asList(
            new Mushroom("Aasšampinjon", "agaricus_arvensis", 0),
            new Mushroom("Punane kärbseseen", "amanita_muscaria", 2),
            new Mushroom("Roheline kärbseseen", "amanita_phalloides", 2),
            new Mushroom("Valge kärbseseen","amanita_virosa", 2),
            new Mushroom(" Põhja-külmaseen", "armillaria_borealis", 0),
            new Mushroom("Harilik kivipuravik","boletus_edulis", 0),
            new Mushroom("Männi-kivipuravik","boletus_pinophilus", 0),
            new Mushroom("Harilik kukeseen","cantharellus_cibarius", 0),
            new Mushroom("Voldiline tindik","coprinopsis_atramentaria", 3),
            new Mushroom("Soomustindik","coprinus_comatus", 0),
            new Mushroom("Kitsemampel","cortinarius_caperatus", 0),
            new Mushroom("Verev vöödik","cortinarius_sanguineus", 2),
            new Mushroom("Kühmvöödik","cortinarius_ubellus", 2),
            new Mushroom("Harilik ringik","cudonia_circinans", 2),
            new Mushroom("Puidu-sametkõrges","flammulina_velutipes", 0),
            new Mushroom("Jahutanuk","galerina_marginata", 2),
            new Mushroom("Kevadkogrits","gyromitra_esculenta", 1),
            new Mushroom("Sügiskogrits","gyromitra_infula", 1),
            new Mushroom("Pruun sametpuravik","imleria_badia", 0),
            new Mushroom("Harilik kännumampel","kuehneromyces_mutabilis", 0),
            new Mushroom("Kollariisikas (Võiseen)","lactarius_scrobiculatus", 1),
            new Mushroom("Kuuseriisikas","lactarius_deterrimus", 0),
            new Mushroom("Männiriisikas","lactarius_rufus", 0),
            new Mushroom("Kaseriisikas","lactarius_torminosus", 1),
            new Mushroom("Tavariisikas","lactarius_trivialis", 0),
            new Mushroom("Tõmmuriisikas","lactarius_turpis", 0),
            new Mushroom("Haavapuravik","leccinum_aurantiacum", 0),
            new Mushroom("Haisev harisirmik","lepiota_cristata", 2),
            new Mushroom("Lilla ebaheinik","lepista_nuda", 0),
            new Mushroom("Suur sirmik","macrolepiot_procera", 0),
            new Mushroom("Kuhikmürkel","morchella_conica", 0),
            new Mushroom("Lilla mütsik","mycena_pura", 2),
            new Mushroom("Tavavahelik","paxillus_involutus", 2),
            new Mushroom("Kuldmampel","phaeolepiota_aurea", 2),
            new Mushroom("Austerservik","pleurotus_ostreatus", 0),
            new Mushroom("Kasepilvik","russula_aeruginea", 0),
            new Mushroom("Kollane pilvik","russula_claroflava", 0),
            new Mushroom("Soopilvik","russula_paludosa", 0),
            new Mushroom("Veinpunane pilvik","russula_vinosa", 0),
            new Mushroom("Võitatik","suillus_luteus", 0),
            new Mushroom("Hobuheinik","tricholoma_equestre", 0),
            new Mushroom("Seepheinik","tricholoma_saponaceum", 2),
            new Mushroom("Kurrel","verpa_bohemica", 2)
    );

    private String [] labels = {
            "agaricus_arvensis", "amanita_muscaria", "amanita_phalloides",
            "amanita_virosa", "armillaria_borealis", "boletus_edulis",
            "boletus_pinophilus", "cantharellus_cibarius", "coprinopsis_atramentaria",
            "coprinus_comatus", "cortinarius_caperatus", "cortinarius_sanguineus",
            "cortinarius_ubellus", "cudonia_circinans", "flammulina_velutipes",
            "galerina_marginata", "gyromitra_esculenta", "gyromitra_infula",
            "imleria_badia", "kuehneromyces_mutabilis", "lactarius_deterrimus",
            "lactarius_rufus", "lactarius_scrobiculatus", "lactarius_torminosus",
            "lactarius_trivialis", "lactarius_turpis", "leccinum_aurantiacum",
            "lepiota_cristata", "lepista_nuda", "macrolepiot_procera", "morchella_conica",
            "mycena_pura", "paxillus_involutus", "phaeolepiota_aurea",
            "pleurotus_ostreatus", "russula_aeruginea", "russula_claroflava",
            "russula_paludosa", "russula_vinosa", "suillus_luteus", "tricholoma_equestre",
            "tricholoma_saponaceum", "verpa_bohemica"
    };

    public MushroomDAO() {
        loadLabels();
    }

    public Mushroom getById(int id) {
        for (Mushroom m : mushrooms) {
            if (m.getID() == id) return m;
        }
        return null;
    }

    public Mushroom getByBinomialName(String name) {
        for (Mushroom m : mushrooms) {
            if (m.getBinomialName().equals(name)) return m;
        }
        return null;
    }

    public List<Mushroom> getAllMushrooms() {
        return mushrooms;
    }

    public void loadLabels() {
        for (int i = 0; i < labels.length; i++) {
            Mushroom m = getByBinomialName(labels[i]);
            m.setID(i);
            //String s = "\"" + m.getEstonianName().toLowerCase() + "\"" + ": " + "\"" + m.getBinomialName().toLowerCase() + "\"";
            //Log.i("VAJALIK", s);
        }
    }

    public List<Mushroom> getById(int[] idArray) {
        List<Mushroom> result = new ArrayList<>();
        for (int id : idArray) {
            result.add(getById(id));
        }
        return result;
    }

}
