package com.svym.inventory.service.medicinedailycostsummary;

import com.svym.inventory.service.dto.MedicineDailyCostSummaryDTO;
import com.svym.inventory.service.entity.MedicineDailyCostSummary;
import com.svym.inventory.service.entity.mapper.MedicineDailyCostSummaryMapper;
import com.svym.inventory.service.repository.MedicineDailyCostSummaryRepository;
import com.svym.inventory.service.repository.MedicineRepository;
import com.svym.inventory.service.location.LocationRepository;
import com.svym.inventory.service.deliverycenter.DeliveryCenterRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineDailyCostSummaryServiceImpl implements MedicineDailyCostSummaryService {

    private final MedicineDailyCostSummaryRepository repository;
    private final MedicineDailyCostSummaryMapper mapper;
    private final MedicineRepository medicineRepository;
    private final LocationRepository locationRepository;
    private final DeliveryCenterRepository deliveryCenterRepository;

    @Override
    @Transactional
    public MedicineDailyCostSummaryDTO create(MedicineDailyCostSummaryDTO dto) {
        // Validate medicine exists
        if (!medicineRepository.existsById(dto.getMedicineId())) {
            throw new EntityNotFoundException("Medicine not found with ID: " + dto.getMedicineId());
        }

        // Validate location exists
        if (!locationRepository.existsById(dto.getLocationId())) {
            throw new EntityNotFoundException("Location not found with ID: " + dto.getLocationId());
        }

        // Validate delivery center exists if provided
        if (dto.getDeliveryCenterId() != null && !deliveryCenterRepository.existsById(dto.getDeliveryCenterId())) {
            throw new EntityNotFoundException("DeliveryCenter not found with ID: " + dto.getDeliveryCenterId());
        }

        MedicineDailyCostSummary entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public MedicineDailyCostSummaryDTO update(Long id, MedicineDailyCostSummaryDTO dto) {
        MedicineDailyCostSummary entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MedicineDailyCostSummary not found with ID: " + id));

        // Validate medicine exists if medicine ID is being updated
        if (!dto.getMedicineId().equals(entity.getMedicineId()) && !medicineRepository.existsById(dto.getMedicineId())) {
            throw new EntityNotFoundException("Medicine not found with ID: " + dto.getMedicineId());
        }

        // Validate location exists if location ID is being updated
        if (!dto.getLocationId().equals(entity.getLocationId()) && !locationRepository.existsById(dto.getLocationId())) {
            throw new EntityNotFoundException("Location not found with ID: " + dto.getLocationId());
        }

        mapper.updateEntityFromDto(dto, entity);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MedicineDailyCostSummary entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MedicineDailyCostSummary not found with ID: " + id));
        repository.delete(entity);
    }

    @Override
    public MedicineDailyCostSummaryDTO getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("MedicineDailyCostSummary not found with ID: " + id));
    }

    @Override
    public List<MedicineDailyCostSummaryDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public MedicineDailyCostSummaryDTO getByMedicineLocationAndDate(Long medicineId, Long locationId, LocalDate distDate) {
        return repository.findByMedicineIdAndLocationIdAndDistDate(medicineId, locationId, distDate)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public MedicineDailyCostSummaryDTO getByMedicineLocationDeliveryCenterAndDate(Long medicineId, Long locationId, Long deliveryCenterId, LocalDate distDate) {
        return repository.findByMedicineIdAndLocationIdAndDeliveryCenterIdAndDistDate(medicineId, locationId, deliveryCenterId, distDate)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public List<MedicineDailyCostSummaryDTO> getByDate(LocalDate distDate) {
        return repository.findByDistDate(distDate)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicineDailyCostSummaryDTO> getByDateRange(LocalDate startDate, LocalDate endDate) {
        return repository.findByDistDateBetween(startDate, endDate)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicineDailyCostSummaryDTO> getByMedicine(Long medicineId) {
        return repository.findByMedicineId(medicineId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicineDailyCostSummaryDTO> getByLocation(Long locationId) {
        return repository.findByLocationId(locationId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicineDailyCostSummaryDTO> getByMedicineAndLocation(Long medicineId, Long locationId) {
        return repository.findByMedicineIdAndLocationId(medicineId, locationId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicineDailyCostSummaryDTO> getByMedicineAndDateRange(Long medicineId, LocalDate startDate, LocalDate endDate) {
        return repository.findByMedicineIdAndDistDateBetween(medicineId, startDate, endDate)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicineDailyCostSummaryDTO> getByLocationAndDateRange(Long locationId, LocalDate startDate, LocalDate endDate) {
        return repository.findByLocationIdAndDistDateBetween(locationId, startDate, endDate)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Double getTotalCostByMedicineLocationAndDateRange(Long medicineId, Long locationId, LocalDate startDate, LocalDate endDate) {
        return repository.getTotalCostByMedicineLocationAndDateRange(medicineId, locationId, startDate, endDate);
    }

    @Override
    public Integer getTotalUnitsDistributedByMedicineLocationAndDateRange(Long medicineId, Long locationId, LocalDate startDate, LocalDate endDate) {
        return repository.getTotalUnitsDistributedByMedicineLocationAndDateRange(medicineId, locationId, startDate, endDate);
    }

    @Override
    @Transactional
    public MedicineDailyCostSummaryDTO createOrUpdateSummary(Long medicineId, Long locationId, Long deliveryCenterId, LocalDate distDate, Integer numberOfUnit, Double totalPrice) {
        // Check if a summary already exists for this combination including delivery center
        Optional<MedicineDailyCostSummary> existingSummary = repository.findByMedicineIdAndLocationIdAndDeliveryCenterIdAndDistDate(medicineId, locationId, deliveryCenterId, distDate);

        if (existingSummary.isPresent()) {
            // Update existing summary by adding to the existing values
            MedicineDailyCostSummary summary = existingSummary.get();
            summary.setNumberOfUnit(summary.getNumberOfUnit() + numberOfUnit);
            summary.setTotalPrice(summary.getTotalPrice() + totalPrice);
            summary = repository.save(summary);
            return mapper.toDto(summary);
        } else {
            // Create new summary
            MedicineDailyCostSummaryDTO dto = new MedicineDailyCostSummaryDTO();
            dto.setMedicineId(medicineId);
            dto.setLocationId(locationId);
            dto.setDeliveryCenterId(deliveryCenterId);
            dto.setDistDate(distDate);
            dto.setNumberOfUnit(numberOfUnit);
            dto.setTotalPrice(totalPrice);
            return create(dto);
        }
    }

    @Override
    @Transactional
    public MedicineDailyCostSummaryDTO createOrUpdateSummary(Long medicineId, Long locationId, LocalDate distDate, Integer numberOfUnit, Double totalPrice) {
        // Legacy method - check if a summary already exists for this combination (without delivery center)
        Optional<MedicineDailyCostSummary> existingSummary = repository.findByMedicineIdAndLocationIdAndDistDate(medicineId, locationId, distDate);

        if (existingSummary.isPresent()) {
            // Update existing summary by adding to the existing values
            MedicineDailyCostSummary summary = existingSummary.get();
            summary.setNumberOfUnit(summary.getNumberOfUnit() + numberOfUnit);
            summary.setTotalPrice(summary.getTotalPrice() + totalPrice);
            summary = repository.save(summary);
            return mapper.toDto(summary);
        } else {
            // Create new summary without delivery center ID
            MedicineDailyCostSummaryDTO dto = new MedicineDailyCostSummaryDTO();
            dto.setMedicineId(medicineId);
            dto.setLocationId(locationId);
            dto.setDistDate(distDate);
            dto.setNumberOfUnit(numberOfUnit);
            dto.setTotalPrice(totalPrice);
            return create(dto);
        }
    }

    @Override
    public List<MedicineDailyCostSummaryDTO> getByLocationAndDateRangeOrderByDateAndCost(Long locationId, LocalDate startDate, LocalDate endDate) {
        return repository.findByLocationAndDateRangeOrderByDateAndCost(locationId, startDate, endDate)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicineDailyCostSummaryDTO> getByDeliveryCenterAndDateRange(Long deliveryCenterId, LocalDate startDate, LocalDate endDate) {
        // Use existing repository method with delivery center filter
        return repository.findByDeliveryCenterIdAndDistDateBetween(deliveryCenterId, startDate, endDate)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicineDailyCostSummaryDTO> getByLocationAndDeliveryCenterAndDateRange(List<Long> locationIds, Long deliveryCenterId, LocalDate startDate, LocalDate endDate) {
        if (locationIds == null || locationIds.isEmpty()) {
            return getByDeliveryCenterAndDateRange(deliveryCenterId, startDate, endDate);
        }

        // Get results for all locations and filter by delivery center
        List<MedicineDailyCostSummaryDTO> allResults = locationIds.stream()
                .flatMap(locationId -> repository.findByLocationIdAndDistDateBetween(locationId, startDate, endDate).stream())
                .map(mapper::toDto)
                .filter(dto -> dto.getDeliveryCenterId() != null && dto.getDeliveryCenterId().equals(deliveryCenterId))
                .collect(Collectors.toList());

        return allResults;
    }

    @Override
    public List<MedicineDailyCostSummaryDTO> getViewDataWithFilters(List<Long> locationIds, Long deliveryCenterId, LocalDate startDate, LocalDate endDate) {
        List<MedicineDailyCostSummaryDTO> results;

        if (locationIds != null && !locationIds.isEmpty() && deliveryCenterId != null && startDate != null && endDate != null) {
            results = getByLocationAndDeliveryCenterAndDateRange(locationIds, deliveryCenterId, startDate, endDate);
        } else if (locationIds != null && !locationIds.isEmpty() && startDate != null && endDate != null) {
            // Filter by multiple locations and date range
            results = locationIds.stream()
                    .flatMap(locationId -> getByLocationAndDateRange(locationId, startDate, endDate).stream())
                    .collect(Collectors.toList());
        } else if (deliveryCenterId != null && startDate != null && endDate != null) {
            results = getByDeliveryCenterAndDateRange(deliveryCenterId, startDate, endDate);
        } else if (startDate != null && endDate != null) {
            results = getByDateRange(startDate, endDate);
        } else if (locationIds != null && !locationIds.isEmpty() && deliveryCenterId != null) {
            // Filter by multiple locations and delivery center
            results = locationIds.stream()
                    .flatMap(locationId -> getByLocation(locationId).stream())
                    .filter(dto -> dto.getDeliveryCenterId() != null && dto.getDeliveryCenterId().equals(deliveryCenterId))
                    .collect(Collectors.toList());
        } else if (locationIds != null && !locationIds.isEmpty()) {
            // Filter by multiple locations only
            results = locationIds.stream()
                    .flatMap(locationId -> getByLocation(locationId).stream())
                    .collect(Collectors.toList());
        } else if (deliveryCenterId != null) {
            results = getAll().stream()
                    .filter(dto -> dto.getDeliveryCenterId() != null && dto.getDeliveryCenterId().equals(deliveryCenterId))
                    .collect(Collectors.toList());
        } else {
            results = getAll();
        }

        return results;
    }
}
