package dpc.fr.back.service;

import dpc.fr.back.entity.Insurance;
import dpc.fr.back.repository.InsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceService {
    @Autowired
     InsuranceRepository insuranceRepository;

    public InsuranceService(InsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
    }

    public Insurance createInsurance(Insurance insurance) {
        return insuranceRepository.save(insurance);
    }

    public Insurance getInsuranceById(int id) {
        try {
            return insuranceRepository.findById(id)
                    .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Insurance> getAllInsurances() {
        return insuranceRepository.findAll();
    }

    public Insurance updateInsurance(int id, Insurance updatedInsurance) {
        Insurance insurance = getInsuranceById(id);
        insurance.setName(updatedInsurance.getName());
        insurance.setNumContrat(updatedInsurance.getNumContrat());
        insurance.setAgency(updatedInsurance.getAgency());
        insurance.setValidityFrom(updatedInsurance.getValidityFrom());
        insurance.setValidityTo(updatedInsurance.getValidityTo());
        return insuranceRepository.save(insurance);
    }

}
