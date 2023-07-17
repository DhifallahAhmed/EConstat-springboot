package dpc.fr.back.controller;

import dpc.fr.back.dto.ConstatDto;
import dpc.fr.back.entity.*;
import dpc.fr.back.service.ConstatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("constat")
public class ConstatController {
    @Autowired
    private ConstatService constatService;

    @PostMapping
    public ResponseEntity<Constat> addNewConstat(@RequestBody ConstatDto constatDto) {
        return constatService.addNewConstat(constatDto);
    }
    @GetMapping("/getConstat/{idU}")
    public ResponseEntity<String> getConstat(@PathVariable("idU") int idU) {
        return constatService.getConstat(idU);
    }
}
