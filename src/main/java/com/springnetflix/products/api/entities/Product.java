package com.springnetflix.products.api.entities;

import com.springnetflix.products.api.data.vo.ProductVO;
import lombok.*;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 255)
    private String name;
    @Column(nullable = false, length = 10)
    private Integer quantity;
    @Column(nullable = false, length = 10)
    private Double price;

    public static Product create(ProductVO productVO) {
        return new ModelMapper().map(productVO, Product.class);
    }

}
