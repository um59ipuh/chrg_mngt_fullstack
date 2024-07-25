package com.dcs.api.crm.customs;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class CustomPage<T> {
  List<T> content;
  CustomPageable page;

  public CustomPage(Page<T> originalPage) {
    this.content = originalPage.getContent();
    this.page =
        new CustomPageable(
            originalPage.getPageable().getPageNumber(),
            originalPage.getPageable().getPageSize(),
            originalPage.getTotalElements(),
            originalPage.getTotalPages());
  }

  @Data
  class CustomPageable {
    int number;
    int size;
    long totalElements;
    int totalPages;

    CustomPageable(int number, int size, long totalElements, int totalPages) {

      this.number = number;
      this.size = size;
      this.totalElements = totalElements;
      this.totalPages = totalPages;
    }
  }
}
