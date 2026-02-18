package com.example

import com.example.dto.ConstitutionDTO
import com.example.mappers.ConstitutionMapper


class ConstitutionController extends GlobalExceptionHandlerController {
    ConstitutionService constitutionService

    def index(String date) {
        List<Constitution> constitutions = constitutionService.findAllByDate(date)
        List<ConstitutionDTO> response = ConstitutionMapper.toDTOList(constitutions)
        respond response
    }

    def show(Long id) {
        Constitution constitution = constitutionService.findById(id)
        ConstitutionDTO response = ConstitutionMapper.toDTO(constitution)
        if (response == null) {
            respond null, status: 404
            return
        }
        respond response
    }
}
