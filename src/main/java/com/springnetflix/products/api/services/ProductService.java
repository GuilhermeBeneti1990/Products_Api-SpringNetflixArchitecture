package com.springnetflix.products.api.services;

import com.springnetflix.products.api.data.vo.ProductVO;
import com.springnetflix.products.api.entities.Product;
import com.springnetflix.products.api.exceptions.ResourceNotFoundException;
import com.springnetflix.products.api.message.ProductSendMessage;
import com.springnetflix.products.api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductSendMessage productSendMessage;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductSendMessage productSendMessage) {
        this.productRepository = productRepository;
        this.productSendMessage = productSendMessage;
    }

    private ProductVO convertToProductVO(Product product) {
        return ProductVO.create(product);
    }

    public ProductVO create(ProductVO productVO) {
        ProductVO productVOCreated = ProductVO.create(productRepository.save(Product.create(productVO)));
        productSendMessage.sendMessage(productVOCreated);
        return productVOCreated;
    }

    public Page<ProductVO> findAll(Pageable pageable) {
        var page = productRepository.findAll(pageable);
        return page.map(this::convertToProductVO);
    }

    public ProductVO findById(Long id) {
        var entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found!"));
        return ProductVO.create(entity);
    }

    public ProductVO update(ProductVO productVO) {
        final Optional<Product> optionalProduct = productRepository.findById(productVO.getId());

        if(!optionalProduct.isPresent()) {
            throw new ResourceNotFoundException("No records found for this id: " + productVO.getId());
        }

        return ProductVO.create(productRepository.save(Product.create(productVO)));

    }

    public void delete(Long id) {
        var entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found!"));

        productRepository.delete(entity);
    }

}
