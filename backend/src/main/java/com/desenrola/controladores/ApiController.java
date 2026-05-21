package com.desenrola.controladores;

import com.desenrola.servicos.DemoDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final DemoDataService demoDataService;

    public ApiController(DemoDataService demoDataService) {
        this.demoDataService = demoDataService;
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(demoDataService.health());
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(demoDataService.login(payload));
    }

    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(demoDataService.registerUser(payload));
    }

    @PostMapping("/professionals/register")
    public ResponseEntity<?> registerProfessional(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(demoDataService.registerProfessional(payload));
    }

    @GetMapping("/categories")
    public ResponseEntity<?> categories() {
        return ResponseEntity.ok(demoDataService.getCategories());
    }

    @GetMapping("/home")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok(demoDataService.getHomeSections());
    }

    @GetMapping("/search")
    public ResponseEntity<?> search() {
        return ResponseEntity.ok(demoDataService.getSearchData());
    }

    @GetMapping("/professionals")
    public ResponseEntity<?> professionals() {
        return ResponseEntity.ok(demoDataService.getProfessionals());
    }

    @GetMapping("/professionals/{professionalId}")
    public ResponseEntity<?> professional(@PathVariable long professionalId) {
        return ResponseEntity.ok(demoDataService.getProfessionalById(professionalId));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(demoDataService.getProfile(userId));
    }

    @GetMapping("/chats")
    public ResponseEntity<?> chats() {
        return ResponseEntity.ok(demoDataService.getChats());
    }

    @GetMapping("/chats/{chatId}")
    public ResponseEntity<?> chat(@PathVariable long chatId) {
        return ResponseEntity.ok(demoDataService.getChat(chatId, null));
    }

    @GetMapping("/chats/by-professional/{professionalId}")
    public ResponseEntity<?> chatByProfessional(@PathVariable long professionalId) {
        return ResponseEntity.ok(demoDataService.getChat(null, professionalId));
    }

    @PostMapping("/chats/{chatId}/messages")
    public ResponseEntity<?> sendMessage(@PathVariable long chatId, @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(demoDataService.sendMessage(chatId, null, payload));
    }

    @PostMapping("/chats/by-professional/{professionalId}/messages")
    public ResponseEntity<?> sendMessageToProfessional(@PathVariable long professionalId, @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(demoDataService.sendMessage(null, professionalId, payload));
    }

    @GetMapping("/services/history")
    public ResponseEntity<?> servicesHistory() {
        return ResponseEntity.ok(demoDataService.getServiceHistory());
    }

    @GetMapping("/services/budgets/{id}")
    public ResponseEntity<?> budget(@PathVariable long id) {
        return ResponseEntity.ok(demoDataService.getBudgetById(id));
    }

    @PostMapping("/services/budgets/{id}/decision")
    public ResponseEntity<?> budgetDecision(@PathVariable long id, @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(demoDataService.decideBudget(id, payload));
    }

    @GetMapping("/services/tracking/{id}")
    public ResponseEntity<?> tracking(@PathVariable long id) {
        return ResponseEntity.ok(demoDataService.getTrackingById(id));
    }

    @GetMapping("/services/completed/{id}")
    public ResponseEntity<?> completed(@PathVariable long id) {
        return ResponseEntity.ok(demoDataService.getCompletedServiceById(id));
    }

    @GetMapping("/services/cancelled/{id}")
    public ResponseEntity<?> cancelled(@PathVariable long id) {
        return ResponseEntity.ok(demoDataService.getCancelledServiceById(id));
    }

    @PostMapping("/services/requests")
    public ResponseEntity<?> createRequest(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(demoDataService.createServiceRequest(payload));
    }

    @PostMapping("/services/reviews")
    public ResponseEntity<?> createReview(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(demoDataService.submitReview(payload));
    }
}
