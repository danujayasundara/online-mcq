package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.tomcat.util.json.JSONParser;
import org.hibernate.annotations.Any;
import org.hibernate.boot.archive.scan.spi.ClassDescriptor.Categorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AnswerDto;
import com.example.demo.dto.AnswerDtoRes;
import com.example.demo.dto.EndExamStatusDTO;
import com.example.demo.dto.ExamAttemptDto;
import com.example.demo.dto.ExamAttendedDto;
import com.example.demo.dto.ExamDto;
import com.example.demo.dto.ExamDtoRes;

import com.example.demo.dto.QuesDto;
import com.example.demo.dto.QuesDtoRes;
import com.example.demo.dto.StudentStatusDto;
import com.example.demo.model.Answer;
import com.example.demo.model.Exam;
import com.example.demo.model.ExamAttempt;
import com.example.demo.model.Question;
import com.example.demo.model.User;
import com.example.demo.repositories.ExamAttemptRepository;
import com.example.demo.repositories.ExamRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.AnswerServiceImpl;
import com.example.demo.service.ExamAttemptServiceImpl;
import com.example.demo.service.ExamService;
import com.example.demo.service.ExamServiceImpl;
import com.example.demo.service.QuestionServiceImpl;
import com.example.demo.service.UserServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
//@RequestMapping("/exampage")
//@CrossOrigin("*")
@CrossOrigin(origins = "http://localhost:8080")
public class ExamController {

	@Autowired
	private ExamService examService;
	
	@Autowired
	private ExamServiceImpl examServiceImpl;
	
	@Autowired
	private QuestionServiceImpl questionServiceImpl;
	
	@Autowired
	private ExamRepository examRepository;
	
	@Autowired 
	private AnswerServiceImpl answerServiceImpl;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ExamAttemptRepository examAttemptRepository;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private ExamAttemptServiceImpl examAttemptServiceImpl;
	
	
	
	  @GetMapping("/exams") 
	  public String getAllExams(Model model) { 
		  List<Exam> exams = examService.getAllExams(); 
		  model.addAttribute("exams", exams);
		  return "exam"; 
	  }
	  
	  //show exam form
	  @GetMapping("/add")
	    public String showNewExamForm(Model model) {
	        model.addAttribute("exam", new Exam());
	        return "saveExam"; 
	    }
	  
	  //add new exam page
	  @GetMapping("/addnewExam")
	  public String newExamPage(Model model) {
	        model.addAttribute("exam", new Exam());
	        return "newExamTeacher";
	    }
	  
	  //save exam
	  @PostMapping("/saveExam")
	  public String saveExam(@RequestBody String req) {
			 
		  /*var response = {
				  redirectionPath: "",
				  
		  }*/
		  
		  ObjectMapper mapper = new ObjectMapper();
		  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	        ExamDto exam;
			try {
				exam = mapper.readValue(req, ExamDto.class);
				System.out.println("FROM FRONTEND " +req);
				System.out.println(exam.getDuration());
				System.out.println(exam.getDateTime());
				System.out.println(exam.getExamName());
				//System.out.println(exam.getQuestion().get(0));
				//System.out.println(exam.getQuestion().getAnswer1().getAnswer());
				
				Exam savedExam = examServiceImpl.saveExam(exam);
				
				/*Long id = savedExam.getExam_id();
				Exam e = examServiceImpl.getExamDetailsById(id);
				System.out.println("New saved exam");
				System.out.println(e);
				
				/*Long id = savedExam.getExam_id();
				Exam e = examServiceImpl.getExamDetailsById(id);
				System.out.println("New saved exam");
				System.out.println(e.getExam_name());
				System.out.println(e.getExam_id());
				System.out.println(e.getQuestions());*/
				
				/*String examJson = mapper.writeValueAsString(exam);
				System.out.println("Serialized Exam JSON: " + examJson);*/
				
				ExamDtoRes examdtoRes = new ExamDtoRes();
				examdtoRes.setExamId(savedExam.getExam_id());
				examdtoRes.setExamName(savedExam.getExam_name());
				examdtoRes.setDuration(savedExam.getDuration());
				
				DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String customFormattedDate = savedExam.getDateTime().format(customFormatter);
				examdtoRes.setDateTime(customFormattedDate);
				
		        
			/*
				List<QuesDtoRes> questionDtoResList = new ArrayList<>();
				for (Question question : savedExam.getQuestions()) {
				    QuesDtoRes questionRes = new QuesDtoRes();
				    questionRes.setQuestionId(question.getQuestion_id()); 
				    questionRes.setQuestion(question.getQuestion_content());

				    List<AnswerDtoRes> answerDtoResList = new ArrayList<>();
				    for (Answer answer : question.getAnswers()) {
				        AnswerDtoRes answerRes = new AnswerDtoRes();
				        answerRes.setAnswerId(answer.getAnswer_id());
				        answerRes.setAnswer(answer.getAnswer());
				        answerRes.setCorrectAnswer(answer.getCorrectAnswer());
				        answerDtoResList.add(answerRes);
				    }
				    questionRes.setAnswer1(answerDtoResList.get(0)); 
				    questionRes.setAnswer2(answerDtoResList.get(1));
				    questionRes.setAnswer3(answerDtoResList.get(2));
				    questionRes.setAnswer4(answerDtoResList.get(3)); 

				    questionDtoResList.add(questionRes);
				}

				examdtoRes.setQuestion(questionDtoResList);
				
				RequestDto response = new RequestDto();
		        response.setDirectionPath("/addnewExam");
		        response.setExamDtoRes(examdtoRes);
				return response;
				*/
				//System.out.print(savedExam.getQuestions());
				//System.out.print(savedExam.getQuestions().get(0).getQuestion_id());
				
				
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
		  
		  	//System.out.println("Ab");
		
		  	return "redirect:/newExamTeacher";
		  	//return null;
	    }
	  
	  //update existing exam
	 @PutMapping("/updateExam/{examId}")
	  public ResponseEntity<Exam> updateExam(@PathVariable Long examId,@RequestBody String req) {
		  ObjectMapper mapper = new ObjectMapper();
		  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	        ExamDto exam;
			try {
				exam = mapper.readValue(req, ExamDto.class);
				System.out.println("FROM FRONTEND for update" +req);
				System.out.println(exam.getDuration());
				System.out.println(exam.getDateTime());
				System.out.println(exam.getExamName());
				
				Exam updatedExam = examServiceImpl.updateExam(exam);
				return ResponseEntity.ok(updatedExam);	
			
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ResponseEntity.notFound().build();
			}
			
	    }
	  
	  
	  private Long getUserIdByEmail(String email) {
		  return userRepository.findByEmail(email).getId();
	  }

	  //show exams for students
	  @GetMapping("/stuexam") 
	  public String showExamList(Model model) { 
		  List<Exam> activeExams = examServiceImpl.getAllActiveExams();
	      model.addAttribute("exams", activeExams);
		  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	      String loggedInUsername = authentication.getName(); 
	      Long userId = getUserIdByEmail(loggedInUsername);
	      
	      User user = userServiceImpl.getUserById(userId);
	      List<ExamAttempt> userAttempts = examAttemptRepository.findByUser(user);
	      for (Exam exam : activeExams) {
	          Long examId = exam.getExam_id();
	          System.out.println("Exam Id" +examId);
	          Exam exam2 = examServiceImpl.getExamById(examId);
	          System.out.println("Exam Details" +exam2);
	          System.out.println("User Details" +user);
	          ExamAttempt examAttempt = examAttemptServiceImpl.getAttemptStatusByExamAndUser(user, exam2);
	          model.addAttribute("examAttempt", examAttempt);
	          System.out.println("Exam Attemot" +examAttempt);
	         // boolean att = examAttempt.getAttempt_status();
	          //System.out.println("Exam Attemot Status" +att); 
	      }
	      List<ExamAttendedDto> examAttendedDtos = new ArrayList<>();

	        // Create a map to store attempt status for each exam
	        Map<Long, Boolean> attemptStatusMap = new HashMap<>();
	        for (ExamAttempt attempt : userAttempts) {
	            attemptStatusMap.put(attempt.getExam().getExam_id(), attempt.getAttempt_status());
	        }

	        // Iterate through active exams and add attempt status to the DTO list
	        for (Exam exam : activeExams) {
	            Long examId = exam.getExam_id();
	            boolean attended = attemptStatusMap.getOrDefault(examId, false); // Default to false if not found
	            ExamAttendedDto dto = new ExamAttendedDto(examId, attended);
	            examAttendedDtos.add(dto);
	        }

	        model.addAttribute("examAttemptDtos", examAttendedDtos);
	      
		  //model.addAttribute("exams", exams); 
		  model.addAttribute("loggedInUserId", userId); 
		  return "stuexam"; 
	  }
	 
	//get exam by id
	  @GetMapping("/getData/{id}")
	  public String getExamById(@PathVariable Long id, Model model) {
		  Exam exam = examService.getExamById(id);
		  model.addAttribute("exams",exam);
		  
		  return "exam";
	  }
	  
	  @GetMapping("/exampaper") 
	  public String showExamPaperPage() {
		 questionServiceImpl.getAllQuestions();
		 return "exampaper"; 
	  }
		 
	 //save exam attempt details
	  @PostMapping("/saveAttemptData")
	  public ResponseEntity<String> saveData(@RequestBody String data) {
		    ObjectMapper mapper = new ObjectMapper();
		    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		    ExamAttemptDto examAtt;

		    try {
		        examAtt = mapper.readValue(data, ExamAttemptDto.class);
		        System.out.println("Exam Att details");
		        System.out.println("ExamId: " + examAtt.getExamId());
		        System.out.println("UserId: " + examAtt.getUserId());

		        ExamAttempt examAttempt = new ExamAttempt();
		        examAttempt.setExam(examAtt.getExamId());
		        User user = userRepository.findById(examAtt.getUserId().getId())
		                .orElseThrow(() -> new IllegalArgumentException("User not found"));

		        examAttempt.setUser(user);
		        examAttemptRepository.save(examAttempt);

		        // Return a success response
		        return ResponseEntity.ok("Exam attempt data saved successfully.");
		    } catch (JsonMappingException e) {
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error mapping JSON.");
		    } catch (JsonProcessingException e) {
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing JSON.");
		    }
		}

	  
	  //update attempt status
	  @PostMapping("/completeAttempt")
	  public String saveAttemptStatus(@RequestBody String data) {
		  ObjectMapper mapper = new ObjectMapper();
		  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		  ExamAttemptDto examAtt;
			try {
				examAtt = mapper.readValue(data, ExamAttemptDto.class);
				User user = userRepository.findById(examAtt.getUserId().getId())
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
				examServiceImpl.saveAttemptStatus(user, examAtt.getExamId());
				
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  return "sturesult";
	  }
	  
	  @GetMapping("/ongoingExam/{examId}")
	  public String displayAndHandleExam(@PathVariable Long examId, Model model) {
		  
		  Exam exam = examServiceImpl.getExamById(examId); 
		    List<ExamAttempt> examAttempts = examAttemptRepository.findByExam(exam);
		    System.out.println("Exam attmpt" +examAttempts);
		    
		    Integer duration = examServiceImpl.getExamDuration(examId);
		    System.out.println("Duration for frontend "+duration);
		    
		    List<StudentStatusDto> attemptInfos = new ArrayList<>();
		    for (ExamAttempt attempt : examAttempts) {
		        String fullName = attempt.getUser().getFullname();
		        boolean status = attempt.getAttempt_status();
		        attemptInfos.add(new StudentStatusDto(fullName, status));
		    }
		    model.addAttribute("attemptInfos", attemptInfos); 
		    model.addAttribute("examAttempts", examAttempts);
		    model.addAttribute("duration", duration);
		
		  return "handleExam";
	  }
	  
	  @PutMapping("/exams/{examId}/status")
	  public String updateExamStatus(@PathVariable Long examId, @RequestBody Map<String, Boolean> requestBody) {
		  Boolean status = requestBody.get("status");
		    if (status != null) {
		        boolean updateSuccessful = examServiceImpl.updateExamStatus(examId, status);
		        
		    } else {
		        return "Status parameter missing or invalid";
		    }
		  
		  return "handleExam";
	  }
	  
	  //send exam details
	  @GetMapping("/exam/{examId}")
	    public ResponseEntity<ExamDtoRes> getExamDetails(@PathVariable("examId") Long examId, Model model) {
	        try {
	            ExamDtoRes examDto = examServiceImpl.getExamDetailsById(examId);
	            System.out.println("Exam Details *****" +examDto);
	            model.addAttribute("examDto", examDto);
	            return ResponseEntity.ok(examDto);
	        } catch (NotFoundException e) {
	        	e.printStackTrace();
	        	return ResponseEntity.notFound().build();	        }
	    }
	  
	  //end exam button
	  @PostMapping("/end-exam/{examId}")
	  public ResponseEntity<Void> endExam(@PathVariable Long examId, @RequestBody EndExamStatusDTO endExamStatusDTO) {
		    examServiceImpl.markExamAsEnded(endExamStatusDTO.getExamId());
		    System.out.println("Exam ID ended: " + examId);
		    System.out.println("Exam ended successfully.");

		    return ResponseEntity.ok().build(); 
		}
	  
	  @GetMapping("/ended-exams")
	  public ResponseEntity<List<EndExamStatusDTO>> getEndedExams() {
		    List<Long> endedExamIds = examServiceImpl.getEndedExams(); 

		    List<EndExamStatusDTO> endExamStatusDTOList = new ArrayList<>();
		    for (Long examId : endedExamIds) {
		        EndExamStatusDTO dto = new EndExamStatusDTO();
		        dto.setExamId(examId);
		        dto.setEnded(true); 
		        endExamStatusDTOList.add(dto);
		    }
		    System.out.println("Ended exam list"+endExamStatusDTOList);

		    return ResponseEntity.ok(endExamStatusDTOList);
		}
	  
	  //get duration
	  @GetMapping("/getduration/{examId}")
	  public ResponseEntity<Map<String, Object>> getExamDuration(@PathVariable Long examId) {
	      Integer duration = examServiceImpl.getExamDuration(examId);
	      if (duration != null) {
	          Map<String, Object> response = new HashMap<>();
	          response.put("duration", duration);
	          System.out.println("*********duration for front*******" + duration);
	          return ResponseEntity.ok(response);
	      } else {
	          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	      }
	  }
	  
	  
	  //get all attempting students
	  @GetMapping("/exam/{examId}/students")
	    public ResponseEntity<List<StudentStatusDto>> getAttemptedStudents(@PathVariable Long examId) {
		  Exam exam = examServiceImpl.getExamById(examId); 
		  System.out.println("Attempted Students List:");
	      List<StudentStatusDto> attemptedStudents = examServiceImpl.getAttemptedStudentsForExam(exam);
	      for (StudentStatusDto student : attemptedStudents) {
	          System.out.println(student); 
	      }
	      return ResponseEntity.ok(attemptedStudents);
	   }
	  
	  

	  //send attended status
	 /* @GetMapping("/exam-statuses")
	  public ResponseEntity<List<ExamAttendedDto>> getExamStatuses(Principal principal) {
	      try {
	          String username = principal.getName();
	          User user = userRepository.findByEmail(username);
	          List<ExamAttempt> userAttempts = examAttemptRepository.findByUser(user);

	          List<ExamAttendedDto> examAttendedDtos = new ArrayList<>();
	          for (ExamAttempt attempt : userAttempts) {
	              boolean attended = attempt.getAttempt_status(); // Check the attempt status
	              Exam exam = attempt.getExam();

	              ExamAttendedDto dto = new ExamAttendedDto(exam.getExam_id(), attempt.getAttempt_status());
	              examAttendedDtos.add(dto);
	          }

	          System.out.println("***********************************\n******************\n");
	          System.out.println("EXAM ATTEMPT" + examAttendedDtos);

	          return ResponseEntity.ok(examAttendedDtos);
	      } catch (Exception e) {
	          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	      }
	  }*/

	  //update teacher table
	  /*@GetMapping("/students/{examId}")
	  public String getStudentsByExamId(@PathVariable Exam examId, Model model) {
	      // Fetch students attempting the specified exam from ExamAttempt table
	      List<ExamAttemptDto> examAttempts = examServiceImpl.getAttemptsByExamId(examId); // Example method to fetch attempts by examId
	      List<User> userIds = examAttempts.stream().map(ExamAttemptDto::getUserId).collect(Collectors.toList());

	      // Fetch student names from User table using userIds
	      List<User> students = userServiceImpl.getStudentsById(userIds); // Example method to fetch students by userIds
	      model.addAttribute("students", students);

	      // Fetch attempt statuses from ExamAttempt table using userIds and examId
	      //List<ExamAttempt> statuses = examAttemptRepository.findByUserAndExam(userIds, examId); // Example method to fetch statuses
	      //model.addAttribute("statuses", statuses);

	      return "handleExam"; // Assuming "handle_result.html" is your Thymeleaf template for the teacher side
	  }*/

	  

}
