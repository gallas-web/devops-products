package com.example.productcrud.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistDto {
    private Long id;
    private List<WishlistItemDto> items;
    private Integer itemCount;
    private LocalDateTime updatedAt;
}
