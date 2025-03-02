package com.akash.blog.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akash.blog.backend.dto.CategoryDto;
import com.akash.blog.backend.entity.Category;
import com.akash.blog.backend.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<CategoryDto> findAllCategories() {
		return categoryRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}
	
	 public CategoryDto createCategory(CategoryDto categoryDto) {
	        Category category = new Category();
	        category.setName(categoryDto.getName());
	        category = categoryRepository.save(category);
	        return new CategoryDto(category.getId(), category.getName());
	    }
	
	private CategoryDto convertToDto(Category category) {
	    CategoryDto categoryDto = new CategoryDto();
	    categoryDto.setId(category.getId());
	    categoryDto.setName(category.getName());
	    return categoryDto;
	}
}
