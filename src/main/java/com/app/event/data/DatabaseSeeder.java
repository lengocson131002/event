package com.app.event.data;

import com.app.event.entity.Lecturer;
import com.app.event.entity.Major;
import com.app.event.entity.Subject;
import com.app.event.repository.LecturerRepository;
import com.app.event.repository.MajorRepository;
import com.app.event.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(value = 1)
public class DatabaseSeeder implements CommandLineRunner {

    private final SubjectRepository subjectRepository;

    private final LecturerRepository lecturerRepository;

    private final MajorRepository majorRepository;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (majorRepository.count() == 0) {
            List<Major> majors = Arrays.asList(
                    new Major("SE", "Software Engineering", "https://swp-event.s3.ap-southeast-1.amazonaws.com/1688090836694how-to-become-software-engineer.jpg"),
                    new Major("AI", "Artificial Intelligent", "https://swp-event.s3.ap-southeast-1.amazonaws.com/1688090819139ai.jpg"),
                    new Major("IA", "Information Assurance", "https://swp-event.s3.ap-southeast-1.amazonaws.com/1688090796250Information-Assurance-vs.-Cybersecurity-1-1.jpg"),
                    new Major("GD", "Graphical Design", "https://swp-event.s3.ap-southeast-1.amazonaws.com/1688090765692what-is-graphic-design-header-1200.png")
            );
            majorRepository.saveAll(majors);
        }

        if (lecturerRepository.count() == 0) {
            List<Lecturer> lecturers = Arrays.asList(
                    new Lecturer()
                            .setName("Nguyễn Văn A")
                            .setCode("LEC_1234")
                            .setPhone("0367537979")
                            .setEmail("nguyenvana@fpt.edu.vn"),
                    new Lecturer()
                            .setName("Nguyễn Thị B")
                            .setCode("LEC_1233")
                            .setPhone("0372292786")
                            .setEmail("nguyenthib@fpt.edu.vn"),
                    new Lecturer()
                            .setName("Nguyễn Văn C")
                            .setCode("LEC_1134")
                            .setPhone("0372296788")
                            .setEmail("nguyenvanc@fpt.edu.vn"),
                    new Lecturer()
                            .setName("Nguyễn Công D")
                            .setCode("LEC_1294")
                            .setPhone("0372296566")
                            .setEmail("nguyencongd@fpt.edu.vn")
            );
            lecturerRepository.saveAll(lecturers);
        }

        if (subjectRepository.count() == 0) {
            Subject mas = new Subject()
                    .setCode("MAS291")
                    .setEnName("Probability & statistics")
                    .setVnName("Xác xuất thống kê")
                    .setMajors(new HashSet<>(
                            Arrays.asList(
                                    majorRepository.findByCode("SE").orElse(null),
                                    majorRepository.findByCode("AI").orElse(null)
                            )
                    ));

            Subject prn211 = new Subject()
                    .setCode("PRN211")
                    .setEnName("Basic Cross-Platform Application Programming With .NET")
                    .setVnName("Lập trình .NET")
                    .setMajors(new HashSet<>(
                            Arrays.asList(
                                    majorRepository.findByCode("SE").orElse(null)
                            )
                    ));


            Subject sc211 = new Subject()
                    .setCode("SC211")
                    .setEnName("Basic Information Security")
                    .setVnName("Nền tảng bảo mật thông tin")
                    .setMajors(new HashSet<>(
                            Arrays.asList(
                                    majorRepository.findByCode("IA").orElse(null)
                            )
                    ));

            subjectRepository.saveAll(Arrays.asList(
                    mas,
                    prn211,
                    sc211
            ));
        }
    }
}
