package com.isco.upc.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isco.upc.app.domain.Company;

public interface PersonRepositoryCustom {

	 Page<Company> findCustomAll(Pageable pageable);
}
