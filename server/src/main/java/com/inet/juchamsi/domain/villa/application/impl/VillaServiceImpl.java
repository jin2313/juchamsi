package com.inet.juchamsi.domain.villa.application.impl;

import com.inet.juchamsi.domain.villa.application.VillaService;
import com.inet.juchamsi.domain.villa.dao.VillaRepository;
import com.inet.juchamsi.domain.villa.dto.request.CreateVillaRequest;
import com.inet.juchamsi.domain.villa.dto.request.ModifyVillaRequest;
import com.inet.juchamsi.domain.villa.dto.response.VillaResponse;
import com.inet.juchamsi.domain.villa.entity.Villa;
import com.inet.juchamsi.global.error.AlreadyExistException;
import com.inet.juchamsi.global.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.inet.juchamsi.global.common.Active.ACTIVE;
import static com.inet.juchamsi.global.common.Active.DISABLED;

@Service
@RequiredArgsConstructor
public class VillaServiceImpl implements VillaService {

    private final VillaRepository villaRepository;

    @Override
    public Long createVilla(CreateVillaRequest request) {
        String[] regionAddressList = request.getRegionAddress().split(" ");
        String regionZipCode = regionAddressList[regionAddressList.length - 1];
        String idNumber = request.getRoadZipCode() + regionZipCode;

        Optional<Long> existedVillaId = villaRepository.existIdNumber(idNumber);
        if(existedVillaId.isPresent()) {
            throw new AlreadyExistException(Villa.class, existedVillaId.get());
        }

        Villa villa = Villa.builder()
                .name(request.getName())
                .address(request.getRoadAddress())
                .idNumber(idNumber)
                .totalCount(request.getTotalCount())
                .active(ACTIVE)
                .build();
        Villa saveVilla = villaRepository.save(villa);

        return saveVilla.getId();
    }


    @Override
    public VillaResponse showDetailVilla(Long villaId) {
        Optional<Villa> targetVilla = villaRepository.findById(villaId);

        if(!targetVilla.isPresent() || targetVilla.get().getActive().equals(DISABLED)) {
            throw new NotFoundException(Villa.class, villaId);
        }

        Villa villa = targetVilla.get();
        VillaResponse response = VillaResponse.builder()
                .id(villa.getId())
                .name(villa.getName())
                .address(villa.getAddress())
                .idNumber(villa.getIdNumber())
                .totalCount(villa.getTotalCount())
                .createDate(villa.getCreatedDate())
                .lastModifiedDate(villa.getLastModifiedDate())
                .build();

        return response;
    }


    @Override
    @Transactional
    public void modifyVilla(ModifyVillaRequest request) {
        Optional<Villa> targetVilla = villaRepository.findById(request.getId());

        if(!targetVilla.isPresent() || targetVilla.get().getActive().equals(DISABLED)) {
            throw new NotFoundException(Villa.class, request.getId());
        }

        Villa villa = targetVilla.get();
        villa.setName(request.getName());
    }

    @Override
    @Transactional
    public void removeVilla(Long villaId) {
        Optional<Villa> targetVilla = villaRepository.findById(villaId);

        if(!targetVilla.isPresent() || targetVilla.get().getActive().equals(DISABLED)) {
            throw new NotFoundException(Villa.class, villaId);
        }

        Villa villa = targetVilla.get();
        villa.setActive(DISABLED);
    }

}