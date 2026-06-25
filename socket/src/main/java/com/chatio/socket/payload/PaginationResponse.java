package com.chatio.socket.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse <T>{

    private T data;
    private int currentPage;
    private long totalItems;
    private int totalPages;
    
}