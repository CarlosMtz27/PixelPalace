const productItems = document.querySelectorAll('.product-item');
productItems.forEach(item => {
	item.addEventListener('mouseover', () => {
		item.style.boxShadow = '0 4px 8px rgba(0, 0, 0, 0.2)';
		item.style.transform = 'scale(1.05)';
		item.style.transition = 'box-shadow 0.3s ease-in-out, transform 0.3s ease-in-out';
	});
	item.addEventListener('mouseout', () => {
		item.style.boxShadow = '0 4px 6px rgba(0, 0, 0, 0.1)';
		item.style.transform = 'scale(1)';
	});
});