package home.project.am.dto;

import java.util.List;

public class PagedResponseDTO<T> {
	private List<T> content;
	private int totalPages;
	private long totalElements;
	private int currentPage;

	public PagedResponseDTO(List<T> content, int totalPages, long totalElements, int currentPage) {
		this.content = content;
	    this.totalPages = totalPages;
	    this.totalElements = totalElements;
	    this.currentPage = currentPage;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
