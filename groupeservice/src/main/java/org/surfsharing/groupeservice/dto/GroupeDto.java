package org.surfsharing.groupeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupeDto implements Serializable {
    Long id;
    String title;
    Long adminId;
    boolean deleted;

}