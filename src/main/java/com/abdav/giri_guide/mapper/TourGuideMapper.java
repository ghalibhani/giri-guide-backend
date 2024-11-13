package com.abdav.giri_guide.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.abdav.giri_guide.constant.EDepositStatus;
import com.abdav.giri_guide.constant.ETransactionStatus;
import com.abdav.giri_guide.entity.DepositHistory;
import com.abdav.giri_guide.entity.GuideReview;
import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.entity.TourGuideHikingPoint;
import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.model.response.TourGuideDetailResponse;
import com.abdav.giri_guide.model.response.TourGuideStatsResponse;
import com.abdav.giri_guide.model.response.TourGuideListResponse;
import com.abdav.giri_guide.model.response.TourGuideProfileResponse;
import com.abdav.giri_guide.util.UrlUtil;

import jakarta.servlet.http.HttpServletRequest;

public class TourGuideMapper {
    private TourGuideMapper() {
    }

    public static TourGuideProfileResponse toTourGuideProfileResponse(
            TourGuide tourGuide,
            HttpServletRequest httpReq

    ) {
        AverageRating averageRating = countAverageRating(tourGuide.getReviews());
        TransactionStats stats = countTransactionStats(tourGuide.getTransactions());

        int totalDoneReject = stats.done() + stats.reject();
        Double doneRate = Double.valueOf(stats.done()) / Double.valueOf(totalDoneReject) * 100;
        Double rejectRate = Double.valueOf(stats.reject()) / Double.valueOf(totalDoneReject) * 100;
        return new TourGuideProfileResponse(
                tourGuide.getUsers().getId(),
                tourGuide.getId(),
                tourGuide.getUsers().getEmail(),
                tourGuide.getName(),
                tourGuide.getGender(),
                tourGuide.getNik(),
                tourGuide.getBirthDate(),
                tourGuide.getDescription(),
                tourGuide.getAddress(),
                tourGuide.getMaxHiker(),
                tourGuide.getPrice(),
                tourGuide.getAdditionalPrice(),
                tourGuide.getTotalPorter(),
                tourGuide.getPricePorter(),
                (tourGuide.getImage() == null) ? null : UrlUtil.resolveImageUrl(tourGuide.getImage(), httpReq),

                stats.done(),
                (stats.total().equals(0)) ? 0 : doneRate.intValue(),
                (stats.total().equals(0)) ? 0 : rejectRate.intValue(),
                averageRating.rating,
                averageRating.totalReview,
                tourGuide.getBankAccount()

        );
    }

    public static TourGuideDetailResponse toTourGuideDetailResponse(
            TourGuide tourGuide,
            List<TourGuideHikingPoint> tourGuideHikingPoint,
            HttpServletRequest httpReq

    ) {
        AverageRating averageRating = countAverageRating(tourGuide.getReviews());
        TransactionStats stats = countTransactionStats(tourGuide.getTransactions());

        int totalDoneReject = stats.done() + stats.reject();
        Double doneRate = Double.valueOf(stats.done()) / Double.valueOf(totalDoneReject) * 100;
        Double rejectRate = Double.valueOf(stats.reject()) / Double.valueOf(totalDoneReject) * 100;
        return new TourGuideDetailResponse(
                tourGuide.getId(),
                tourGuide.getName(),
                (tourGuide.getImage() == null) ? null : UrlUtil.resolveImageUrl(tourGuide.getImage(), httpReq),
                tourGuide.getDescription(),
                tourGuide.isActive(),
                averageRating.rating,
                averageRating.totalReview,

                stats.done(),
                (stats.total().equals(0)) ? 0 : doneRate.intValue(),
                (stats.total().equals(0)) ? 0 : rejectRate.intValue(),
                tourGuide.getMaxHiker(),
                tourGuide.getPrice(),
                tourGuide.getAdditionalPrice(),
                tourGuide.getTotalPorter(),
                tourGuide.getPricePorter(),
                TourGuideHikingPointMapper.toListMountainListHikingPoint(tourGuideHikingPoint)

        );
    }

    public static List<TourGuideListResponse> toListOfTourGuideListResponse(
            List<TourGuide> tourGuides, HttpServletRequest httpReq) {

        List<TourGuideListResponse> result = new ArrayList<>();
        for (TourGuide tourGuide : tourGuides) {
            AverageRating averageRating = countAverageRating(tourGuide.getReviews());
            result.add(new TourGuideListResponse(
                    tourGuide.getId(),
                    tourGuide.getName(),
                    (tourGuide.getImage() == null) ? null : UrlUtil.resolveImageUrl(tourGuide.getImage(), httpReq),
                    tourGuide.getDescription(),
                    averageRating.rating,
                    averageRating.totalReview));
        }
        return result;
    }

    public static TourGuideStatsResponse toTourGuideStatsResponse(
            TourGuide tourGuide, List<DepositHistory> histories, LocalDateTime nearestSchedule) {

        TotalIncomeAndWithdraw depositData = countIncomeAndWithdraw(histories);
        TransactionStats stats = countTransactionStats(tourGuide.getTransactions());
        return new TourGuideStatsResponse(
                tourGuide.getDeposit().getMoney(),
                depositData.totalIncome(),
                depositData.totalWithdraw(),
                stats.done(),
                stats.reject(),
                nearestSchedule,
                stats.upcoming(),
                stats.waitingApprove()

        );
    }

    private record AverageRating(
            Double rating,
            Integer totalReview) {
    }

    private record TotalIncomeAndWithdraw(
            Long totalIncome,
            Long totalWithdraw) {
    }

    private record TransactionStats(
            Integer total,
            Integer done,
            Integer reject,
            Integer waitingApprove,
            Integer upcoming) {
    }

    private static AverageRating countAverageRating(List<GuideReview> reviews) {
        Double totalRating = 0.0;
        Integer totalReview = 0;

        for (GuideReview review : reviews) {
            if (review.getDeletedDate() == null) {
                totalRating += Double.valueOf(review.getRating());
                totalReview += 1;
            }
        }

        return new AverageRating(
                (totalReview > 0) ? totalRating / Double.valueOf(totalReview) : 0.0,
                totalReview);

    }

    private static TotalIncomeAndWithdraw countIncomeAndWithdraw(List<DepositHistory> histories) {
        Long totalIncome = 0L;
        Long totalWithdraw = 0L;

        for (DepositHistory history : histories) {
            if (history.getStatus().equals(EDepositStatus.IN)) {
                totalIncome += history.getNominal();
            } else if (history.getStatus().equals(EDepositStatus.OUT)) {
                totalWithdraw += history.getNominal();
            }
        }

        return new TotalIncomeAndWithdraw(totalIncome, totalWithdraw);
    }

    private static TransactionStats countTransactionStats(List<Transaction> transactions) {
        Integer total = 0;
        Integer done = 0;
        Integer reject = 0;
        Integer waitingApprove = 0;
        Integer upcoming = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getStatus().equals(ETransactionStatus.DONE)) {
                done += 1;
                total += 1;
            } else if (transaction.getStatus().equals(ETransactionStatus.REJECTED)) {
                reject += 1;
                total += 1;
            } else if (transaction.getStatus().equals(ETransactionStatus.WAITING_APPROVE)) {
                waitingApprove += 1;
            } else if (transaction.getStatus().equals(ETransactionStatus.UPCOMING)) {
                upcoming += 1;
                total += 1;
            }
        }

        return new TransactionStats(total, done, reject, waitingApprove, upcoming);
    }
}
