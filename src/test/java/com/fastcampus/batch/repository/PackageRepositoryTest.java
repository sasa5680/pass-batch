package com.fastcampus.batch.repository;

import com.fastcampus.batch.repository.packaze.PackageEntity;
import com.fastcampus.batch.repository.packaze.PackageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class PackageRepositoryTest {

    @Autowired
    PackageRepository packageRepository;

    @Test
    public void test_save(){
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디 챌린지 PT 12주");
        packageEntity.setPeriod(84);

        //when
        packageRepository.save(packageEntity);

        //then
        assertNotNull(packageEntity.getPackageSeq());
    }

    @Test
    public void test_findByCreatedAfter() {
        LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(1);

        PackageEntity packageEntity0 = new PackageEntity();
        packageEntity0.setPackageName("학생 전용 3개월");
        packageEntity0.setPeriod(20);
        packageRepository.save(packageEntity0);

        PackageEntity packageEntity1 = new PackageEntity();
        packageEntity1.setPackageName("학생 전용 6개월");
        packageEntity1.setPeriod(180);
        packageRepository.save(packageEntity1);

        //when
        final List<PackageEntity> packageEntityList =
                packageRepository.findByCreatedAtAfter(localDateTime, PageRequest.of(0, 1));

        //then
        assertEquals(1, packageEntityList.size());
        assertEquals(packageEntity1.getPackageSeq(), packageEntityList.get(0).getPackageSeq());
    }

    @Test
    public void test_updateCountAndPeriod() {

        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디프로필 이벤트 4개월");
        packageEntity.setPeriod(90);
        packageRepository.save(packageEntity);

        //when
        int updatedCount = packageRepository.updateCountAndPeriod(packageEntity.getPackageSeq(), 30, 120);
        final PackageEntity updatedPackage = packageRepository.findById(packageEntity.getPackageSeq()).get();

        //then
        assertEquals(1, updatedCount);
        assertEquals(30,(int) updatedPackage.getCount());
        assertEquals(120,(int) updatedPackage.getPeriod());
    }
}
