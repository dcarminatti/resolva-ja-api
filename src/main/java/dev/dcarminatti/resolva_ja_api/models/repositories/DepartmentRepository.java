package dev.dcarminatti.resolva_ja_api.models.repositories;

import dev.dcarminatti.resolva_ja_api.models.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}

