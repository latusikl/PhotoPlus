import { Component, OnInit } from '@angular/core';
import { CategoryService } from "../../services/category/category.service";
import { Category } from '../../models/category/category';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent implements OnInit {

  categories: Category[];

  constructor( private categoryService: CategoryService ) { }

  ngOnInit(): void {
      this.categoryService.getAll().subscribe((data: Category[]) => {
        this.categories = data;
      });
  }

}
