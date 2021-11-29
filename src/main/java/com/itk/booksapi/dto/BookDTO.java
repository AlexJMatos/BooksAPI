package com.itk.booksapi.dto;

public class BookDTO {

		private String isbn;

		private String title;
		
		private String editorial;
		
		private int editionNumber;
		
		private int copyright;

		public String getIsbn() {
			return isbn;
		}

		public void setIsbn(String isbn) {
			this.isbn = isbn;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getEditorial() {
			return editorial;
		}

		public void setEditorial(String editorial) {
			this.editorial = editorial;
		}

		public int getEditionNumber() {
			return editionNumber;
		}

		public void setEditionNumber(int editionNumber) {
			this.editionNumber = editionNumber;
		}

		public int getCopyright() {
			return copyright;
		}

		public void setCopyright(int copyright) {
			this.copyright = copyright;
		}
}