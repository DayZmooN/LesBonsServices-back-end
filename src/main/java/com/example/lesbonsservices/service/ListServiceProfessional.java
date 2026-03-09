package com.example.lesbonsservices.service;

import com.example.lesbonsservices.dto.ServiceListDto;
import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ListServiceProfessional {

    //Couche d'accès aux données JPA
    private final UserRepository userRepository;
    //Injection du professionalRepository
    public ListServiceProfessional(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    //Retourne la liste complète des services par professionalId
    public List<ServiceListDto> findServiceByProfessional(Long connectedUserId){

        User userPro = userRepository.findById(connectedUserId)
        .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, " user not found " + connectedUserId));

        if (userPro.getProfessional() == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès denied");
        }
        return userPro.getProfessional().getServices().stream()
                .map(
                        proService -> {
                            ServiceListDto reponseDto = new ServiceListDto();
                            reponseDto.setName(proService.getName());
                            reponseDto.setDurationMinutes(proService.getDurationMinutes());
                            reponseDto.setPrice(proService.getPrice());
                            return  reponseDto;

                        }).toList();
    }
}
