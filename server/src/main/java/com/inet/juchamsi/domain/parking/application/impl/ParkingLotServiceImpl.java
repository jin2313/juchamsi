package com.inet.juchamsi.domain.parking.application.impl;

import com.inet.juchamsi.domain.parking.application.ParkingLotService;
import com.inet.juchamsi.domain.parking.dao.ParkingLotRepository;
import com.inet.juchamsi.domain.parking.dto.response.ParkingLotResponse;
import com.inet.juchamsi.domain.parking.entity.ParkingLot;
import com.inet.juchamsi.domain.villa.dao.VillaRepository;
import com.inet.juchamsi.domain.villa.entity.Villa;
import com.inet.juchamsi.global.error.AlreadyExistException;
import com.inet.juchamsi.global.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.inet.juchamsi.global.common.Active.ACTIVE;
import static com.inet.juchamsi.global.common.Active.DISABLED;

@Service
@RequiredArgsConstructor
public class ParkingLotServiceImpl implements ParkingLotService {

    private final VillaRepository villaRepository;
    private final ParkingLotRepository parkingLotRepository;

    @Override
    @Transactional
    public void createParkingLot(Long villaId, int parkingLotCol) {
        Optional<Villa> targetVilla = villaRepository.findById(villaId);
        if(targetVilla.isEmpty()) {
            throw new NotFoundException(Villa.class, villaId);
        }

        Villa villa = targetVilla.get();

        Long lotCount = parkingLotRepository.countByVilla(villa);
        if(lotCount > 0) {
            throw new AlreadyExistException(ParkingLot.class, villaId);
        }

        ParkingLot parkingLot;
        for(int i = 1; i <= parkingLotCol; i++) {
            parkingLot = ParkingLot.createFrontParkingLot(villa, i, "**", (i + parkingLotCol), ACTIVE);
            parkingLotRepository.save(parkingLot);

            parkingLot = ParkingLot.createBackParkingLot(villa, (i + parkingLotCol), "**",  i, ACTIVE);
            parkingLotRepository.save(parkingLot);
        }
    }
    
    @Override
    @Transactional
    public void removeParkingLot(String villaIdNumber) {
        List<ParkingLot> parkingLotList = parkingLotRepository.findByIdNumber(villaIdNumber);

        if(parkingLotList == null || parkingLotList.size() == 0) {
            throw new NotFoundException(ParkingLot.class, villaIdNumber);
        }

        for(int i = 0; i < parkingLotList.size(); i++) {
            parkingLotList.get(i).setActive(DISABLED);
        }
    }
}
