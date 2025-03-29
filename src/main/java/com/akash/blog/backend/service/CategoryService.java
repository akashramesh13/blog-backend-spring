package com.akash.blog.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akash.blog.backend.dto.CategoryDto;
import com.akash.blog.backend.entity.Category;
import com.akash.blog.backend.repository.CategoryRepository;

@Service
public class CategoryService {

	private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<CategoryDto> findAllCategories() {
		try {
			return categoryRepository.findAll().stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("Error fetching all categories", e);
			throw new RuntimeException("Failed to fetch categories");
		}
	}
	
	public CategoryDto createCategory(CategoryDto categoryDto) {
		try {
			// Check if category name already exists
			if (categoryRepository.findByName(categoryDto.getName()).isPresent()) {
				throw new RuntimeException("Category with name '" + categoryDto.getName() + "' already exists");
			}

			Category category = new Category();
			category.setName(categoryDto.getName());
			category = categoryRepository.save(category);
			return convertToDto(category);
		} catch (Exception e) {
			logger.error("Error creating category: {}", categoryDto.getName(), e);
			throw new RuntimeException("Failed to create category");
		}
	}

	public CategoryDto updateCategory(String id, CategoryDto categoryDto) {
		try {
			Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Category not found"));

			// Check if new name is already taken by another category
			if (!category.getName().equals(categoryDto.getName())) {
				categoryRepository.findByName(categoryDto.getName())
					.ifPresent(existing -> {
						throw new RuntimeException("Category with name '" + categoryDto.getName() + "' already exists");
					});
			}

			category.setName(categoryDto.getName());
			category = categoryRepository.save(category);
			return convertToDto(category);
		} catch (Exception e) {
			logger.error("Error updating category: {}", id, e);
			throw new RuntimeException("Failed to update category");
		}
	}

	public void deleteCategory(String id) {
		try {
			if (!categoryRepository.existsById(id)) {
				throw new RuntimeException("Category not found");
			}
			categoryRepository.deleteById(id);
		} catch (Exception e) {
			logger.error("Error deleting category: {}", id, e);
			throw new RuntimeException("Failed to delete category");
		}
	}
	
	private CategoryDto convertToDto(Category category) {
		return new CategoryDto(category.getId(), category.getName());
	}
}
