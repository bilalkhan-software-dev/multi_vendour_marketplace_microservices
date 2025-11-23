package com.vendor_marketplace.transaction_report_service.dao.implementation;

import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.transaction_report_service.dao.interfaces.SellerReportDao;
import com.vendor_marketplace.transaction_report_service.dao.repository.SellerReportRepository;
import com.vendor_marketplace.transaction_report_service.models.entity.SellerReport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class SellerReportDaoImpl implements SellerReportDao {

    private final SellerReportRepository reportRepository;


    @Override
    public SellerReport save(SellerReport sellerReport) {
        return reportRepository.save(sellerReport);
    }

    @Override
    public Optional<SellerReport> findById(Long id) {
        return reportRepository.findById(id);
    }

    @Override
    public Optional<SellerReport> findBySellerId(String sellerId) {
        return reportRepository.findBySellerId(sellerId);
    }


    @Override
    public List<SellerReport> findAllReportOfTheSeller(String sellerId, int page, int size) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return reportRepository.findAllBySellerId(sellerId, pageable);

    }

    @Override
    public List<SellerReport> findAllReport(int page, int size) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return reportRepository.findAll(pageable).getContent();

    }

    @Override
    public boolean existsBySellerId(String sellerId) {
        return reportRepository.existsBySellerId(sellerId);
    }

    @Override
    public void deleteSellerReportById(Long id) {

        reportRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Seller report not found")
        );

        reportRepository.deleteById(id);
    }

    @Override
    public void deleteAllReportOfTheSeller(String sellerId) {

        reportRepository.findBySellerId(sellerId).orElseThrow(
                () -> new ResourceNotFoundException("Reports not found with your requested seller id")
        );

        reportRepository.deleteAllBySellerId(sellerId);
    }


}
