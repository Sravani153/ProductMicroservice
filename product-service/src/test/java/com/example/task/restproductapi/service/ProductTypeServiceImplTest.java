 package com.example.task.restproductapi.service;

 import static org.junit.jupiter.api.Assertions.*;
 import static org.mockito.Mockito.*;

 import java.util.*;

 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.api.extension.ExtendWith;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;

 import com.example.task.restproductapi.exceptions.NotFoundException;
 import com.example.task.restproductapi.entities.ProductType;
 import com.example.task.restproductapi.repository.ProductTypeRepository;
 import org.mockito.junit.jupiter.MockitoExtension;

 @ExtendWith(MockitoExtension.class)
  class ProductTypeServiceImplTest {

     @Mock
     private ProductTypeRepository productTypeRepository;

     @InjectMocks
     private ProductTypeServiceImpl productTypeService;

      ProductType productType;
      List<ProductType> productTypeList;

     @BeforeEach
     public void setUp() {
         productType = new ProductType();
         productType.setId(1L);
         productType.setName("Electronics");

         productTypeList=new ArrayList<>();
         productTypeList.add(productType);
     }
     @Test
     public void testCreateProductType() {
         ProductType productType = new ProductType();
         productType.setId(1L);
         productType.setName("Electronics");

         when(productTypeRepository.save(productType)).thenReturn(productType);

         ProductType createdProductType = productTypeService.createProductType(productType);

         assertEquals(productType, createdProductType);
         verify(productTypeRepository, times(1)).save(productType);
     }

     @Test
     public void testGetAllProductTypes() {

         when(productTypeRepository.findAll()).thenReturn(productTypeList);

         List<ProductType> result = productTypeService.getAllProductTypes();

         assertEquals(productTypeList.size(), result.size());
         assertEquals(productTypeList, result);
         verify(productTypeRepository, times(1)).findAll();
     }
    

     @Test
     public void testGetProductTypeById() {

         when(productTypeRepository.findById(1L)).thenReturn(Optional.of(productType));

         ProductType retrievedProductType = productTypeService.getProductTypeById(1L);

         assertEquals(productType, retrievedProductType);
         verify(productTypeRepository, times(1)).findById(1L);
     }

     @Test
     public void testGetProductTypeById_NotFound() {
         when(productTypeRepository.findById(1L)).thenReturn(Optional.empty());

         assertThrows(NotFoundException.class, () -> productTypeService.getProductTypeById(1L));
         verify(productTypeRepository, times(1)).findById(1L);
     }

     @Test
     public void testUpdateProductType() {
         ProductType existingProductType = new ProductType(1L, "Electronics");
         ProductType updatedProductType = new ProductType(1L, "Electronics Updated");

         when(productTypeRepository.findById(1L)).thenReturn(Optional.of(existingProductType));
         when(productTypeRepository.save(existingProductType)).thenReturn(updatedProductType);

         ProductType result = productTypeService.updateProductType(1L, updatedProductType);

         assertEquals(updatedProductType.getName(), result.getName());
         verify(productTypeRepository, times(1)).findById(1L);
         verify(productTypeRepository, times(1)).save(existingProductType);
     }

     @Test
     public void testDeleteProductType() {

         when(productTypeRepository.findById(1L)).thenReturn(Optional.of(productType));

         productTypeService.deleteProductType(1L);

         verify(productTypeRepository, times(1)).delete(productType);
     }
     @Test
     public void testDeleteProductType_NotFound() {
         when(productTypeRepository.findById(1L)).thenReturn(Optional.empty());

         assertThrows(NotFoundException.class, () -> productTypeService.deleteProductType(1L));
         verify(productTypeRepository, times(0)).delete(any());
     }
 }
