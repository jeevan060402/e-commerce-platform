package com.ecommerce.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
	

    private final Map<String, Product> productMap = new HashMap<>();

    // Create Product
    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody Product product) {
        try {
            // Generate unique ProductID (you may use UUID.randomUUID().toString() for simplicity)
            String productId = generateUniqueId();
            product.setProductId(productId);

            // Save the product to the in-memory storage
            productMap.put(productId, product);

            // Return the product details
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Read Product
    @GetMapping("/{productId}")
    public ResponseEntity<Object> readProduct(@PathVariable String productId) {
        try {
            Product product = productMap.get(productId);

            if (product != null) {
                return new ResponseEntity<>(product, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error reading product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Update Product
    @PutMapping("/{productId}")
    public ResponseEntity<Object> updateProduct(@PathVariable String productId, @RequestBody Product updatedProduct) {
        try {
            if (productMap.containsKey(productId)) {
                // Update the product details
                productMap.put(productId, updatedProduct);

                return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Delete Product
    @DeleteMapping("/{productId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable String productId) {
        try {
            if (productMap.containsKey(productId)) {
                // Remove the product from the in-memory storage
                productMap.remove(productId);

                return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting product: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Apply Discount or Tax
    @PatchMapping("/{productId}/apply")
    public ResponseEntity<Object> applyDiscountOrTax(
            @PathVariable String productId,
            @RequestParam String type, // "discount" or "tax"
            @RequestParam double value
    ) {
        try {
            if (productMap.containsKey(productId)) {
                Product product = productMap.get(productId);

                if ("discount".equalsIgnoreCase(type)) {
                    // Apply discount
                    product.setPrice(product.getPrice() * (1 - value / 100));
                } else if ("tax".equalsIgnoreCase(type)) {
                    // Apply tax
                    product.setPrice(product.getPrice() * (1 + value / 100));
                } else {
                    return new ResponseEntity<>("Invalid type. Use 'discount' or 'tax'", HttpStatus.BAD_REQUEST);
                }

                return new ResponseEntity<>("Discount/Tax applied successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error applying discount or tax: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Helper method to generate unique ID (you may use UUID.randomUUID().toString())
    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}
