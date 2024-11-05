package com.abdav.giri_guide.model.response;

import java.util.List;

public record TourGuideDetailResponse(
                String id,
                String name,
                String image,
                String description,
                boolean isActive,

                Double rating,
                Integer totalReview,
                Integer totalCustomer,

                Integer donePercentage,
                Integer rejectPercentage,

                Long price,
                Long additionalPrice,
                Integer totalPorter,
                Long pricePorter,

                List<MountainListHikingPointResponse> mountains

) {
}
