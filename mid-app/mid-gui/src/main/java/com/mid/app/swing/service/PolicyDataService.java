package com.mid.app.swing.service;

import com.mid.app.politem.model.PolItem;
import com.mid.app.politemben.model.PolItemBen;
import com.mid.app.polmaster.model.PolMaster;
import com.mid.app.polmtrveh.model.PolMtrVeh;
import com.mid.app.polrisk.model.PolRisk;
import com.mid.app.swing.model.ImportCriteria;
import com.mid.app.xmm600.model.Xmm600;
import com.mid.app.xmm600.repository.Xmm600Repository;
import com.mid.app.polmaster.repository.PolMasterRepository;
import com.mid.app.polmtrveh.repository.PolMtrVehRepository;
import com.mid.app.polrisk.repository.PolRiskRepository;
import com.mid.app.politem.repository.PolItemRepository;
import com.mid.app.politemben.repository.PolItemBenRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class PolicyDataService {
    
    public List<PolMaster> findPolMasters(EntityManager em, ImportCriteria criteria) {
        PolMasterRepository repo = new PolMasterRepository();
        repo.em = em;
        
        if (criteria.getPolicyNumber() != null && !criteria.getPolicyNumber().isEmpty()) {
            return repo.findPolMasterRecordByPolNo(criteria.getPolicyNumber());
        } else if (criteria.getVehicleRegNo() != null && !criteria.getVehicleRegNo().isEmpty()) {
            PolMtrVehRepository vehRepo = new PolMtrVehRepository();
            vehRepo.em = em;
            String polNo = vehRepo.findPolNoByVehReg(criteria.getVehicleRegNo());
            
            if (polNo != null) {
                return repo.findPolMasterRecordByPolNo(polNo);
            }
            return null;
        } else if (criteria.getStartDate() != null && criteria.getEndDate() != null) {
            return repo.findPolMasterRecordByTranDate(criteria.getStartDate(), criteria.getEndDate());
        }
        
        return null;
    }
    
    public boolean shouldSkipPolicy(EntityManager em, String polNo, Integer renCnt) {
        PolMasterRepository repo = new PolMasterRepository();
        repo.em = em;
        Boolean result = repo.isSkipPolicy(polNo, renCnt);
        return result != null && result;
    }
    
    public List<PolMtrVeh> findVehicles(EntityManager em, PolMaster polMaster, String vehicleRegNo) {
        PolMtrVehRepository repo = new PolMtrVehRepository();
        repo.em = em;
        
        if (vehicleRegNo != null && !vehicleRegNo.isEmpty()) {
            return repo.findPolMtrVehicleByVehRegNo(polMaster.getPolNo(), 
                                                   polMaster.getRenCnt(), 
                                                   polMaster.getEndtCnt(), 
                                                   vehicleRegNo);
        } else {
            return repo.findPolMtrVehicleList(polMaster.getPolNo(), 
                                             polMaster.getRenCnt(), 
                                             polMaster.getEndtCnt());
        }
    }
    
    public List<PolRisk> findRisks(EntityManager em, PolMaster polMaster, PolMtrVeh vehicle) {
        PolRiskRepository repo = new PolRiskRepository();
        repo.em = em;
        
        return repo.findPolRiskRecord(polMaster.getPolNo(), 
                                     polMaster.getRenCnt(), 
                                     polMaster.getEndtCnt(), 
                                     vehicle.getRiskGrp(), 
                                     vehicle.getRiskNo());
    }
    
    public PolItem findItem(EntityManager em, PolMaster polMaster, PolMtrVeh vehicle) {
        PolItemRepository repo = new PolItemRepository();
        repo.em = em;
        
        return repo.findByPrimaryKey(polMaster.getPolNo(), 
                                    polMaster.getRenCnt(), 
                                    polMaster.getEndtCnt(), 
                                    vehicle.getRiskGrp(), 
                                    vehicle.getRiskNo(), 
                                    vehicle.getItemNo());
    }
    
    public List<PolItemBen> findItemBenefits(EntityManager em, PolMaster polMaster, PolMtrVeh vehicle) {
        PolItemBenRepository repo = new PolItemBenRepository();
        repo.em = em;
        
        return repo.findPolItemBenRecord(polMaster.getPolNo(), 
                                        polMaster.getRenCnt(), 
                                        polMaster.getEndtCnt(), 
                                        vehicle.getRiskGrp(), 
                                        vehicle.getRiskNo(), 
                                        vehicle.getItemNo());
    }
    
    public List<Xmm600> findIntermediaries(EntityManager em, PolMaster polMaster) {
        Xmm600Repository repo = new Xmm600Repository();
        repo.em = em;
        
        return repo.findInterMediary(polMaster.getAgent());
    }
    
    public List<Xmm600> findClients(EntityManager em, PolMaster polMaster) {
        Xmm600Repository repo = new Xmm600Repository();
        repo.em = em;
        
        return repo.findClients(polMaster.getInsured());
    }
}