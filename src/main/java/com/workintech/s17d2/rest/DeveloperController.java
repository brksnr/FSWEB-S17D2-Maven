package com.workintech.s17d2.rest;

import com.workintech.s17d2.dto.DeveloperRequest;
import com.workintech.s17d2.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.workintech.s17d2.tax.Taxable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


    @RestController
    @RequestMapping("/developers")
    public class DeveloperController {
        private final Taxable taxable;


    @Autowired
        public DeveloperController( Taxable taxable) {
        System.out.println("bakalım");
            this.taxable = taxable;
        }

    public Map<Integer, Developer> developers;

        @PostConstruct
    public void init(){
         this.developers = new HashMap<>();
            this.developers.put(1, new Developer(1, "Berk", 17.002, Experience.JUNIOR));
            this.developers.put(2, new Developer(2, "Berke", 17.002, Experience.MID));
            this.developers.put(3, new Developer(3, "Berkem", 17.002, Experience.SENIOR));
    }

        @GetMapping
        public List<Developer> getDeveloper(){
            return new ArrayList<>(developers.values());
        }

        @GetMapping("/{id}")
        public Developer getDeveloperById(@PathVariable("id") Integer id){
            return developers.get(id);
        }

        @PostMapping
        public ResponseEntity<Developer> addDeveloper(@RequestBody DeveloperRequest request) {
            Developer newDeveloper;

            // Deneyim değerini enum'a dönüştür
            Experience experience = Experience.fromString(request.getExperience());

            Double salary = request.getSalary();
            switch (experience) {
                case JUNIOR:
                    salary -= salary * (taxable.getSimpleTaxRate() / 100);
                    newDeveloper = new JuniorDeveloper(request.getId(), request.getName(), salary);
                    break;

                case MID:
                    salary -= salary * (taxable.getMiddleTaxRate() / 100);
                    newDeveloper = new MidDeveloper(request.getId(), request.getName(), salary);
                    break;

                case SENIOR:
                    salary -= salary * (taxable.getUpperTaxRate() / 100);
                    newDeveloper = new SeniorDeveloper(request.getId(), request.getName(), salary);
                    break;

                default:
                    throw new IllegalArgumentException("Invalid experience level: " + experience);
            }

            developers.put(request.getId(), newDeveloper);
            return new ResponseEntity<>(newDeveloper, HttpStatus.CREATED);  // 201 dönülür
        }



        @PutMapping("/{id}")
        public ResponseEntity<Developer> updateDeveloper(@PathVariable int id, @RequestBody DeveloperRequest request) {
            if (!developers.containsKey(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }


            Experience experience = Experience.fromString(request.getExperience());

            Double salary = request.getSalary();
            Developer updatedDeveloper;

            switch (experience) {
                case JUNIOR:
                    salary -= salary * (taxable.getSimpleTaxRate() / 100);
                    updatedDeveloper = new JuniorDeveloper(request.getId(), request.getName(), salary);
                    break;

                case MID:
                    salary -= salary * (taxable.getMiddleTaxRate() / 100);
                    updatedDeveloper = new MidDeveloper(request.getId(), request.getName(), salary);
                    break;

                case SENIOR:
                    salary -= salary * (taxable.getUpperTaxRate() / 100);
                    updatedDeveloper = new SeniorDeveloper(request.getId(), request.getName(), salary);
                    break;

                default:
                    throw new IllegalArgumentException("Invalid experience level: " + experience);
            }


            developers.put(id, updatedDeveloper);
            return new ResponseEntity<>(updatedDeveloper, HttpStatus.OK);
        }


        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteDeveloper(@PathVariable int id) {
            if (!developers.containsKey(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            developers.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }









        /*
        [POST]/workintech/developers => id, name, salary ve experience değerlerini alır,
        experience tipine bakarak uygun developer objesini oluşturup developers mapine ekler.
         JuniorDeveloper için salary bilgisinden salarygetSimpleTaxRate() değerini düşmelisiniz.
          Aynı şekilde MidDeveloper için salarygetMiddleTaxRate(), SeniorDeveloper için salary*getUpperTaxRate() değerlerini
           salary bilgisinden düşmelisiniz.
        * */




}
