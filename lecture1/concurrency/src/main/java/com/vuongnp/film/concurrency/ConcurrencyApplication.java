package com.vuongnp.film.concurrency;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vuongnp.film.concurrency.business.BusinessLogic;
import com.vuongnp.film.concurrency.model.Movie;
import com.vuongnp.film.concurrency.model.User;
import com.vuongnp.film.concurrency.repository.InMemoryRepository;
import com.vuongnp.film.concurrency.repository.RepositoryInterface;

@SpringBootApplication
public class ConcurrencyApplication {
	private static int TRANSACTIONS_PER_MACHINE = 1000;

	private static synchronized void job1(BusinessLogic businessLogic, User user) throws Exception {
		// System.out.println("+++");
        businessLogic.addToFavoriteMovies(user, "1");
		businessLogic.rateMovie(user, "1", 3);
    }

	private static synchronized void job2(BusinessLogic businessLogic, User user) throws Exception {
		// System.out.println("---");
		businessLogic.removeFromFavoriteMovies(user, "1");
		businessLogic.rateMovie(user, "1", 1);
    }

	public static void main(String[] args) throws Exception {
		User adam = new User("0", "vuong");
		// System.out.println(adam.toString());
		Movie naruto = new Movie("0", "Naruto", 0);
		// System.out.println(naruto.toString());
		Movie onepiece = new Movie("1", "One piece", 1);
		Movie conan = new Movie("2", "Conan", 0);

		RepositoryInterface inMemoryRepository = new InMemoryRepository();
		BusinessLogic businessLogic = new BusinessLogic(inMemoryRepository);
		businessLogic.addMovie(naruto);
		businessLogic.addMovie(onepiece);
		businessLogic.addMovie(conan);
		
		businessLogic.addToFavoriteMovies(adam, "0");
		businessLogic.addToFavoriteMovies(adam, "0");
		// businessLogic.addToFavoriteMovies(adam, "1");
		// System.out.println(businessLogic.getUserFavoriteMovies(adam));
		// businessLogic.removeFromFavoriteMovies(adam, "1");
		// // businessLogic.removeFromFavoriteMovies(adam, "2");
		// System.out.println(businessLogic.getUserFavoriteMovies(adam));

		businessLogic.rateMovie(adam, "0", 5);
		// businessLogic.rateMovie(adam, "1", 3);
		// // businessLogic.rateMovie(adam, "3", 1);
		// System.out.println(businessLogic.getUserRatedMovies(adam));
		List<Thread> allThreads = new ArrayList<>();
        for (int i = 1; i <= TRANSACTIONS_PER_MACHINE; i++) {
            Thread t = new Thread(new Runnable() {
                public void run() {
					try {
						job1(businessLogic, adam);
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						job2(businessLogic, adam);
					} catch (Exception e) {
						e.printStackTrace();
					}
                }
            });
            t.start();
            allThreads.add(t);
        }
        for (Thread t: allThreads) {
			t.join();
        }
		System.out.println(businessLogic.getUserFavoriteMovies(adam));
		System.out.println(businessLogic.getUserRatedMovies(adam));

	}

}
